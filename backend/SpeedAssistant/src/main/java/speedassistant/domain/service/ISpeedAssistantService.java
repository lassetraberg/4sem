package speedassistant.domain.service;

import commonvehicle.domain.model.vehicledata.GpsCoordinates;
import commonvehicle.domain.model.vehicledata.Vehicle;

import java.util.UUID;

public interface ISpeedAssistantService {
    Short getLatestVelocity(UUID vehicleId);

    double getLatestAcceleration(UUID vehicleId);

    short getLatestSpeedLimit(UUID vehicleId);

    GpsCoordinates getLatestGpsCoordinate(UUID vehicleId);

    Vehicle getLatestVehicleData(UUID vehicleId);

    boolean isSpeeding(UUID vehicleId);

    void publishSpeedingAlarm(UUID vehicleId);

}
