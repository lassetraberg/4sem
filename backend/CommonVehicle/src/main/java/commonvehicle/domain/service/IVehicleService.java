package commonvehicle.domain.service;

import commonAuthentication.domain.repository.IAccountRepository;
import commonvehicle.domain.model.Device;
import commonvehicle.domain.model.vehicledata.Vehicle;
import commonvehicle.domain.repository.IVehicleRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface IVehicleService {
    boolean addData(Vehicle vehicle);

    List<Vehicle> getData(UUID deviceId, String username);

    List<Vehicle> getData(UUID deviceId, String username, String fromDateTime, String toDateTime);

    boolean userOwnsVehicle(UUID deviceId, String username);

    boolean registerDevice(UUID deviceId, String username, String licensePlate);

    boolean deleteDevice(UUID deviceId, String username);

    List<Device> getDevices(String username);

    Device getDevice(String username, String deviceId);

    void setVehicleRepository(IVehicleRepository vehicleRepository);

    void setAccountRepository(IAccountRepository accountRepository);

}
