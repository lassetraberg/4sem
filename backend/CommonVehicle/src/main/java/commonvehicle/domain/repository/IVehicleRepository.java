package commonvehicle.domain.repository;

import commonvehicle.domain.model.Device;
import commonvehicle.domain.model.vehicledata.Vehicle;

import java.time.Instant;
import java.util.List;

public interface IVehicleRepository {
    boolean addData(String deviceId, short speed, double acceleration, short speedLimit, double latitude, double longitude);

    List<Vehicle> getData(String deviceId);

    List<Vehicle> getData(String deviceId, Instant from, Instant to);

    Device getDevice(String deviceId);

    boolean deleteDevice(String deviceId, Long accountId);

    List<Device> getDevices(Long accountId);

    boolean doesUserOwnDevice(String deviceId, String username);

    boolean registerDevice(String deviceId, Long accountId, String licensePlate);

    List<Vehicle> getAllData();

    List<Vehicle> getAllData(Instant from, Instant to);
}
