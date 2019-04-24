package speedassistant.domain.service;

import commonvehicle.domain.model.vehicledata.GpsCoordinates;
import commonvehicle.domain.model.vehicledata.Vehicle;

import java.util.UUID;

public interface ISpeedAssistantService {
    Short getLatestVelocity(UUID deviceId);

    double getLatestAcceleration(UUID deviceId);

    short getLatestSpeedLimit(UUID deviceId);

    GpsCoordinates getLatestGpsCoordinate(UUID deviceId);

    Vehicle getLatestVehicleData(UUID deviceId);

    boolean isSpeeding(UUID deviceId);

    void publishSpeedingAlarm(UUID deviceId, boolean isSpeeding);
}
