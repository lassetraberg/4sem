package commonvehicle.domain.repository;

import common.data.database.DatabaseConnection;
import commonvehicle.domain.model.vehicledata.GpsCoordinates;
import commonvehicle.domain.model.vehicledata.Vehicle;
import commonvehicle.domain.model.vehicledata.Velocity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
            stmt.setString(1, deviceId);
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
            stmt.setString(1, deviceId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
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

                vehicle.set(v);
            }
        });

        return vehicle.get();
    }

    @Override
    public boolean doesUserOwnDevice(String deviceId, String username) {
        String sql = "SELECT connects.device_id FROM device, connects, account WHERE account.username = ? AND account.account_id = connects.account_id";
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
    public boolean registerDevice(String deviceId, Long accountId) {
        String sql = "INSERT INTO connects (device_id, account_id) VALUES (?, ?);";
        AtomicBoolean success = new AtomicBoolean(false);

        this.executeQuery(conn -> {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, deviceId);
            stmt.setLong(2, accountId);

            int res = stmt.executeUpdate();

            if (res != 0) {
                success.set(true);
            }
        });

        return success.get();
    }
}
