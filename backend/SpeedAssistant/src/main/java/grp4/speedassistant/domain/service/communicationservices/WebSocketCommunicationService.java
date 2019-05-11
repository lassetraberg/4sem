package grp4.speedassistant.domain.service.communicationservices;

import com.fasterxml.jackson.databind.ObjectMapper;
import grp4.common.web.websockets.AbstractWebSocketEndpoint;
import grp4.commonvehicle.domain.model.vehicledata.Acceleration;
import grp4.commonvehicle.domain.model.vehicledata.GpsCoordinates;
import grp4.commonvehicle.domain.model.vehicledata.Velocity;
import grp4.speedassistant.domain.dto.GpsDto;
import grp4.speedassistant.domain.dto.SpeedLimitDto;
import grp4.speedassistant.domain.service.ISpeedAssistantService;

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
        SpeedLimitDto speedLimitDto = new SpeedLimitDto(speedAssistantService.getLatestSpeedLimit(deviceId));
        sendTo(deviceId, velocityObject, "velocity");
        sendTo(deviceId, speedLimitDto, "speedLimit");
    }

    @Override
    public void onAccelerationMessage(UUID deviceId) {
        Double acceleration = speedAssistantService.getLatestAcceleration(deviceId);
        Acceleration accelerationObject = new Acceleration(acceleration);
        sendTo(deviceId, accelerationObject, "acceleration");
    }

}
