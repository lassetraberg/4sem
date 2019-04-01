package speedassistant.domain.service;

import speedassistant.domain.VehicleData;

import java.util.UUID;

public interface ISpeedAssistantService {
    VehicleData getVehicleData(UUID deviceId, String username);
}
