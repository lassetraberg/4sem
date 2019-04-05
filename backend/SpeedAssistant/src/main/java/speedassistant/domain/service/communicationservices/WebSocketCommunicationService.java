package speedassistant.domain.service.communicationservices;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.web.websockets.AbstractWebSocketEndpoint;
import commonvehicle.domain.model.vehicledata.Acceleration;
import commonvehicle.domain.model.vehicledata.GpsCoordinates;
import commonvehicle.domain.model.vehicledata.Velocity;
import speedassistant.domain.dto.GpsDto;
import speedassistant.domain.service.ISpeedAssistantService;

import java.util.UUID;

public class WebSocketCommunicationService extends AbstractWebSocketEndpoint<UUID> implements ISpeedAssistantCommunication {
    private ISpeedAssistantService speedAssistantService;

    public WebSocketCommunicationService(String variablePath, ObjectMapper mapper, ISpeedAssistantService speedAssistantService) {
        super(variablePath, mapper);
        this.speedAssistantService = speedAssistantService;
    }

    @Override
    public void onGpsMessage(UUID deviceId) {
        GpsCoordinates gps = speedAssistantService.getLatestGpsCoordinate(deviceId);
        GpsDto gpsDto = new GpsDto(gps);
        sendTo(deviceId, gpsDto, "gps");
    }

    @Override
    public void onVelocityMessage(UUID deviceId) {
        Short velocity = speedAssistantService.getLatestVelocity(deviceId);
        Velocity velocityObject = new Velocity(velocity);
        sendTo(deviceId, velocityObject, "velocity");
    }

    @Override
    public void onAccelerationMessage(UUID deviceId) {
        Double acceleration = speedAssistantService.getLatestAcceleration(deviceId);
        Acceleration accelerationObject = new Acceleration(acceleration);
        sendTo(deviceId, accelerationObject, "acceleration");
    }

}
