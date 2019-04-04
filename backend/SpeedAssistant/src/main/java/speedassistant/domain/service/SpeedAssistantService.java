package speedassistant.domain.service;

import speedassistant.domain.models.vehicledata.Vehicle;

import java.util.UUID;

public class SpeedAssistantService implements ISpeedAssistantService {
    private ISpeedLimitService speedLimitService;

    public SpeedAssistantService(ISpeedLimitService speedLimitService) {
        this.speedLimitService = speedLimitService;
    }

    @Override
    public Vehicle getVehicleData(UUID deviceId, String username) {
        return null;
    }
}
