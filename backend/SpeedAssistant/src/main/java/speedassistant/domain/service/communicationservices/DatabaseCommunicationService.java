package speedassistant.domain.service.communicationservices;

import commonvehicle.domain.repository.IVehicleRepository;

import java.util.UUID;

public class DatabaseCommunicationService implements ISpeedAssistantCommunication {
    private IVehicleRepository vehicleRepository;

    public DatabaseCommunicationService(IVehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public void onGpsMessage(UUID deviceId, String msg) {

    }

    @Override
    public void onVelocityMessage(UUID deviceId, String msg) {

    }
}
