package speedassistant.domain.service.communicationservices;

import commonvehicle.domain.model.vehicledata.Vehicle;
import commonvehicle.domain.service.IVehicleService;
import speedassistant.domain.service.ISpeedAssistantService;

import java.util.UUID;

public class DatabaseCommunicationService implements ISpeedAssistantCommunication {

    private ISpeedAssistantService speedAssistantService;
    private IVehicleService vehicleService;

    private int requestAllowedEveryNSeconds;
    private long lastRequestTime;

    public DatabaseCommunicationService(ISpeedAssistantService speedAssistantService, IVehicleService vehicleService, int rateLimit) {
        this.speedAssistantService = speedAssistantService;
        this.vehicleService = vehicleService;
        this.requestAllowedEveryNSeconds = rateLimit;
    }

    public DatabaseCommunicationService(ISpeedAssistantService speedAssistantService, IVehicleService vehicleService) {
        this(speedAssistantService, vehicleService, 2);
    }

    @Override
    public void onGpsMessage(UUID deviceId) {
        tryAddData(deviceId);
    }

    @Override
    public void onVelocityMessage(UUID deviceId) {
        tryAddData(deviceId);
    }

    @Override
    public void onAccelerationMessage(UUID deviceId) {
        tryAddData(deviceId);
    }

    private void tryAddData(UUID deviceId) {
        double secondDifference = (System.currentTimeMillis() - lastRequestTime) / 1000.0;

        if (secondDifference >= requestAllowedEveryNSeconds) {
            lastRequestTime = System.currentTimeMillis();
            Vehicle vehicle = speedAssistantService.getLatestVehicleData(deviceId);
            if (vehicle == null) {
                return;
            }

            if (!vehicleService.addData(vehicle)) {
                System.out.println(String.format("Failed to log vehicle data for deviceId: %s", deviceId.toString()));
            }
        }
    }
}
