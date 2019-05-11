package grp4.commonvehicle.domain.repository;

import grp4.common.data.database.DatabaseConnection;
import grp4.commonvehicle.domain.model.Device;
import grp4.commonvehicle.domain.model.vehicledata.GpsCoordinates;
import grp4.commonvehicle.domain.model.vehicledata.Vehicle;
import org.postgresql.util.PSQLException;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class VehicleRepository extends DatabaseConnection implements IVehicleRepository {
    @Override
    public boolean addData(String deviceId, short speed, double acceleration, short speedLimit, double latitude, double longitude) {
        String sql = "INSERT INTO vehicle (device_id, speed, acceleration, speed_limit, latitude, longitude) VALUES (?, ?, ?, ?, ?, ?)";

        AtomicBoolean successBool = new AtomicBoolean(false);

        this.executeQuery(conn -> {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setObject(1, deviceId, Types.OTHER);
            stmt.setShort(2, speed);
            stmt.setDouble(3, acceleration);
            stmt.setShort(4, speedLimit);
            stmt.setDouble(5, latitude);
            stmt.setDouble(6, longitude);

            int success = stmt.executeUpdate();
            if (success != 0) {
                successBool.set(true);
            }
        });

        return successBool.get();
    }

    @Override
    public List<Vehicle> getData(String deviceId) {
        Instant from = Instant.parse("1970-01-01T00:00:00.00Z");
        Instant to = Instant.parse("3000-01-01T00:00:00.00Z");
        return this.getData(deviceId, from, to);
    }

    @Override
    public List<Vehicle> getData(String deviceId, Instant from, Instant to) {
        String sql = "SELECT device_id, speed, timestamp, acceleration, speed_limit, latitude, longitude FROM vehicle WHERE device_id = ? AND timestamp BETWEEN ? AND ? ORDER BY timestamp";

        List<Vehicle> vehicleDataList = new ArrayList<>();
        this.executeQuery(conn -> {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setObject(1, deviceId, Types.OTHER);
            stmt.setTimestamp(2, Timestamp.from(from));
            stmt.setTimestamp(3, Timestamp.from(to));


            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Vehicle v = vehicleFromResultSet(rs);
                vehicleDataList.add(v);
            }
        });

        return vehicleDataList;
    }


    @Override
    public List<Vehicle> getAllData() {
        Instant from = Instant.parse("1970-01-01T00:00:00.00Z");
        Instant to = Instant.parse("3000-01-01T00:00:00.00Z");
        return this.getAllData(from, to);
    }

    @Override
    public List<Vehicle> getAllData(Instant from, Instant to) {
        String sql = "SELECT device_id, speed, timestamp, acceleration, speed_limit, latitude, longitude FROM vehicle WHERE timestamp BETWEEN ? AND ? ORDER BY timestamp";
        List<Vehicle> vehicleDataList = new ArrayList<>();
        this.executeQuery(conn -> {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setTimestamp(1, Timestamp.from(from));
            stmt.setTimestamp(2, Timestamp.from(to));


            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Vehicle v = vehicleFromResultSet(rs);
                vehicleDataList.add(v);
            }
        });

        return vehicleDataList;
    }

    @Override
    public Device getDevice(String deviceId) {
        String sql = "SELECT device_id, last_active, license_plate FROM device WHERE device_id = ?";

        AtomicReference<Device> device = new AtomicReference<>();

        this.executeQuery(conn -> {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setObject(1, deviceId, Types.OTHER);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Device dev = deviceFromResultSet(rs);
                device.set(dev);
            }
        });

        return device.get();
    }

    @Override
    public boolean deleteDevice(String deviceId, Long accountId) {
        boolean succesfullyDisconnected = disconnect(deviceId, accountId);
        boolean succfessfullyDeletedData = deleteVehicleData(deviceId);
        String sql = "DELETE FROM device WHERE device_id = ?";

        AtomicBoolean success = new AtomicBoolean(false);

        this.executeQuery(conn -> {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setObject(1, deviceId, Types.OTHER);

            int res = stmt.executeUpdate();
            if (res != 0) {
                success.set(true);
            }
        });

        return success.get();
    }

    @Override
    public List<Device> getDevices(Long accountId) {
        String sql = "SELECT device.device_id, device.last_active, device.license_plate FROM device " +
                "JOIN connects ON connects.device_id = device.device_id where connects.account_id = ?";
        List<Device> deviceList = new ArrayList<>();

        this.executeQuery(conn -> {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, accountId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Device device = deviceFromResultSet(rs);
                deviceList.add(device);
            }
        });

        return deviceList;
    }

    @Override
    public boolean doesUserOwnDevice(String deviceId, String username) {
        String sql = "SELECT connects.device_id FROM device JOIN connects ON connects.device_id = device.device_id " +
                "WHERE connects.account_id = (SELECT account.account_id FROM account WHERE account.username = ?)";
        AtomicBoolean userOwnsDevice = new AtomicBoolean(false);
        UUID deviceUUID = UUID.fromString(deviceId);

        this.executeQuery(conn -> {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);

            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                UUID dbDeviceId = UUID.fromString(res.getString("device_id"));
                if (deviceUUID.equals(dbDeviceId)) {
                    userOwnsDevice.set(true);
                    break;
                }
            }
        });

        return userOwnsDevice.get();
    }

    @Override
    public boolean registerDevice(String deviceId, Long accountId, String licensePlate) {
        boolean deviceCreated = createDevice(deviceId, licensePlate);
        if (!deviceCreated) {
            return false;
        }

        String sql = "INSERT INTO connects (device_id, account_id) VALUES (?, ?);";
        AtomicBoolean success = new AtomicBoolean(false);

        this.executeQuery(conn -> {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setObject(1, deviceId, Types.OTHER);
            stmt.setLong(2, accountId);

            int res = stmt.executeUpdate();

            if (res != 0) {
                success.set(true);
            }
        });

        return success.get();
    }

    private boolean createDevice(String deviceId, String licensePlate) {
        String sql = "INSERT INTO device (device_id, license_plate) VALUES (?, ?);";
        AtomicBoolean success = new AtomicBoolean(false);

        this.executeQuery(conn -> {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setObject(1, deviceId, Types.OTHER);
            stmt.setString(2, licensePlate);

            try {
                int res = stmt.executeUpdate();
                if (res != 0) {
                    success.set(true);
                }
            } catch (PSQLException ex) {
                System.err.println(ex.getServerErrorMessage());
                ex.printStackTrace();
                success.set(false);
            }

        });

        return success.get();
    }

    private boolean disconnect(String deviceId, Long accountId) {
        String sql = "DELETE FROM connects WHERE device_id = ? AND account_id = ?";
        AtomicBoolean success = new AtomicBoolean(false);

        this.executeQuery(conn -> {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setObject(1, deviceId, Types.OTHER);
            stmt.setLong(2, accountId);

            int res = stmt.executeUpdate();
            if (res == 0) { // 0 is successful delete
                success.set(true);
            }
        });

        return success.get();
    }

    private boolean deleteVehicleData(String deviceId) {
        String sql = "DELETE FROM vehicle WHERE device_id = ?";
        AtomicBoolean success = new AtomicBoolean(false);

        this.executeQuery(conn -> {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setObject(1, deviceId, Types.OTHER);

            int res = stmt.executeUpdate();
            if (res == 0) { // 0 is successful delete
                success.set(true);
            }
        });

        return success.get();
    }


    private Device deviceFromResultSet(ResultSet rs) throws SQLException {
        Timestamp timestamp = rs.getTimestamp("last_active");
        String uuid = rs.getString("device_id");
        Device device = new Device(
                UUID.fromString(uuid),
                timestamp != null ? timestamp.toInstant() : null,
                rs.getString("license_plate")
        );

        return device;
    }

    private Vehicle vehicleFromResultSet(ResultSet rs) throws SQLException {
        Vehicle v = new Vehicle(
                UUID.fromString(rs.getString("device_id")),
                rs.getShort("speed"),
                rs.getTimestamp("timestamp").toInstant(),
                rs.getDouble("acceleration"),
                rs.getShort("speed_limit"),
                new GpsCoordinates(
                        rs.getDouble("latitude"), rs.getDouble("longitude")
                )
        );

        return v;
    }
}
