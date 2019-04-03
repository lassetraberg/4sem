package speedassistant.domain.service;

import speedassistant.domain.models.vehicledata.Vehicle;

import java.util.UUID;

public interface ISpeedAssistantService {
    Vehicle getVehicleData(UUID deviceId, String username);
}
