package commonvehicle.domain.repository;

import commonvehicle.domain.model.vehicledata.Vehicle;

public interface IVehicleRepository {
    boolean addData(String deviceId, short speed, double acceleration, short speedLimit, double latitude, double longitude);

    Vehicle getData(String deviceId);

    boolean doesUserOwnDevice(String deviceId, String username);

    boolean registerDevice(String deviceId, Long accountId);
}
