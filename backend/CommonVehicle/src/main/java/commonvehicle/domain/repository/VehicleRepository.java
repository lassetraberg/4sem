package commonvehicle.domain.repository;

import common.data.database.DatabaseConnection;
import commonvehicle.domain.model.Device;
import commonvehicle.domain.model.vehicledata.GpsCoordinates;
import commonvehicle.domain.model.vehicledata.Vehicle;
import commonvehicle.domain.model.vehicledata.Velocity;
import org.postgresql.util.PSQLException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

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
    public Vehicle getData(String deviceId) {
        String sql = "SELECT device_id, speed, timestamp, acceleration, speed_limit, latitude, longitude FROM vehicle WHERE device_id = ?";

        AtomicReference<Vehicle> vehicle = new AtomicReference<>();

        this.executeQuery(conn -> {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setObject(1, deviceId, Types.OTHER);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Vehicle v = vehicleFromResultSet(rs);

                vehicle.set(v);
            }
        });

        return vehicle.get();
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
        if (!succesfullyDisconnected) return false;
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
            if (res != 0) {
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
                new Velocity(rs.getShort("speed")),
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
