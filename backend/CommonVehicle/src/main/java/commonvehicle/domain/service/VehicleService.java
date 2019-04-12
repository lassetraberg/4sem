package commonvehicle.domain.service;


import commonAuthentication.domain.model.Account;
import commonAuthentication.domain.repository.IAccountRepository;
import commonvehicle.domain.model.Device;
import commonvehicle.domain.model.vehicledata.Vehicle;
import commonvehicle.domain.repository.IVehicleRepository;
import io.javalin.NotFoundResponse;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.UUID;

public class VehicleService implements IVehicleService {
    private IVehicleRepository vehicleRepository;
    private IAccountRepository accountRepository;

    private DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
            .parseDefaulting(ChronoField.NANO_OF_SECOND, 0)
            .toFormatter()
            .withZone(ZoneId.of("UTC"));

    public VehicleService(IVehicleRepository vehicleRepository, IAccountRepository accountRepository) {
        this.vehicleRepository = vehicleRepository;
        this.accountRepository = accountRepository;
    }

    public VehicleService() {
    }

    @Override
    public boolean addData(Vehicle vehicle) {
        if (vehicleRepository.getDevice(vehicle.getDeviceId().toString()) == null) {
            return false;
        }
        return vehicleRepository.addData(vehicle.getDeviceId().toString(), vehicle.getVelocity(),
                vehicle.getAcceleration(), vehicle.getSpeedLimit(), vehicle.getGpsCoordinates().getLat(), vehicle.getGpsCoordinates().getLon());
    }

    @Override
    public List<Vehicle> getData(UUID deviceId, String username, String fromDateTime, String toDateTime) {
        if (!userOwnsVehicle(deviceId, username)) {
            throw new NotFoundResponse("Vehicle not found");
        }

        Instant from = null;
        Instant to = null;
        try {
            if (fromDateTime != null && toDateTime != null) {
                from = dateTimeFormatter.parse(fromDateTime, Instant::from);
                to = dateTimeFormatter.parse(toDateTime, Instant::from);
            }
        } catch (DateTimeParseException ex) {
            ex.printStackTrace();
            from = null;
            to = null;
        }

        if (from == null || to == null) {
            return vehicleRepository.getData(deviceId.toString());
        } else {
            return vehicleRepository.getData(deviceId.toString(), from, to);
        }
    }

    @Override
    public boolean userOwnsVehicle(UUID deviceId, String username) {
        return vehicleRepository.doesUserOwnDevice(deviceId.toString(), username);
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
