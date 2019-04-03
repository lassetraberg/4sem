package commonvehicle.domain.service;


import commonAuthentication.domain.model.Account;
import commonAuthentication.domain.repository.IAccountRepository;
import commonvehicle.domain.dto.VehicleDto;
import commonvehicle.domain.model.vehicledata.Vehicle;
import commonvehicle.domain.repository.IVehicleRepository;
import io.javalin.NotFoundResponse;

import java.util.UUID;

public class VehicleService implements IVehicleService {
    private IVehicleRepository vehicleRepository;
    private IAccountRepository accountRepository;

    public VehicleService(IVehicleRepository vehicleRepository, IAccountRepository accountRepository) {
        this.vehicleRepository = vehicleRepository;
        this.accountRepository = accountRepository;
    }

    public VehicleService() {}

    @Override
    public boolean addData(VehicleDto vehicleDto) {
        return vehicleRepository.addData(vehicleDto.getDeviceId().toString(), vehicleDto.getSpeed(), vehicleDto.getAcceleration(),
                vehicleDto.getSpeedLimit(), vehicleDto.getGpsCoordinates().getLat(), vehicleDto.getGpsCoordinates().getLon());
    }

    @Override
    public Vehicle getData(UUID deviceId, String username) {
        if (!userOwnsVehicle(deviceId, username)) {
            throw new NotFoundResponse("Device not found");
        }

        return vehicleRepository.getData(deviceId.toString());
    }

    @Override
    public boolean userOwnsVehicle(UUID vehicleId, String username) {
        return vehicleRepository.doesUserOwnDevice(vehicleId.toString(), username);
    }

    @Override
    public boolean registerDevice(UUID deviceId, String username) {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            return false;
        }

        return vehicleRepository.registerDevice(deviceId.toString(), account.getId());
    }

    @Override
    public void setVehicleRepository(IVehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public void setAccountRepository(IAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

}
