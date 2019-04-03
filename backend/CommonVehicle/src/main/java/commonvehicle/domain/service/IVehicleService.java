package commonvehicle.domain.service;

import commonAuthentication.domain.repository.IAccountRepository;
import commonvehicle.domain.dto.VehicleDto;
import commonvehicle.domain.model.vehicledata.Vehicle;
import commonvehicle.domain.repository.IVehicleRepository;

import java.util.UUID;

public interface IVehicleService {
    boolean addData(VehicleDto vehicleDto);

    Vehicle getData(UUID deviceId, String username);

    boolean userOwnsVehicle(UUID vehicleId, String username);

    boolean registerDevice(UUID deviceId, String username);

    void setVehicleRepository(IVehicleRepository vehicleRepository);

    void setAccountRepository(IAccountRepository accountRepository);
}
