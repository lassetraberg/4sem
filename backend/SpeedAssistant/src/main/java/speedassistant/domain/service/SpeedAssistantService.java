package speedassistant.domain.service;

import speedassistant.domain.VehicleData;

import java.util.UUID;

public class SpeedAssistantService implements ISpeedAssistantService{
    private ISpeedLimitService speedLimitService;

    public SpeedAssistantService(ISpeedLimitService speedLimitService) {
        this.speedLimitService = speedLimitService;
    }

    @Override
    public VehicleData getVehicleData(UUID deviceId, String username) {
        return new VehicleData(null, null);
    }
}
