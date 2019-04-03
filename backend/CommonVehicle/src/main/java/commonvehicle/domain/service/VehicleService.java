package commonvehicle.domain.service;


import commonAuthentication.domain.model.Account;
import commonAuthentication.domain.repository.IAccountRepository;
import commonvehicle.domain.dto.VehicleDto;
import commonvehicle.domain.model.Device;
import commonvehicle.domain.model.vehicledata.Vehicle;
import commonvehicle.domain.repository.IVehicleRepository;
import io.javalin.HttpResponseException;
import io.javalin.NotFoundResponse;

import java.util.ArrayList;
import java.util.List;
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
            throw new NotFoundResponse("Vehicle not found");
        }

        Vehicle vehicle = vehicleRepository.getData(deviceId.toString());

        if (vehicle == null) {
            throw new NotFoundResponse(String.format("No data found for vehicle id %s", deviceId.toString()));
        }

        return vehicle;
    }

    @Override
    public boolean userOwnsVehicle(UUID vehicleId, String username) {
        return vehicleRepository.doesUserOwnDevice(vehicleId.toString(), username);
    }

    @Override
    public boolean registerDevice(UUID deviceId, String username, String licensePlate) {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            return false;
        }

        return vehicleRepository.registerDevice(deviceId.toString(), account.getId(), licensePlate);
    }

    @Override
    public boolean deleteDevice(UUID deviceId, String username) {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            return false;
        }

        return vehicleRepository.deleteDevice(deviceId.toString(), account.getId());
    }

    @Override
    public List<Device> getDevices(String username) {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            return null;
        }

        return vehicleRepository.getDevices(account.getId());
    }

    @Override
    public Device getDevice(String username, String deviceId) {
        if (!vehicleRepository.doesUserOwnDevice(deviceId, username)) {
            throw new NotFoundResponse("Device not found");
        }

        return vehicleRepository.getDevice(deviceId);
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
