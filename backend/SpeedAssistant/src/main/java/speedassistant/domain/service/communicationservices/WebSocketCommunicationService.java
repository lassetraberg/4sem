package speedassistant.domain.service.communicationservices;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.web.websockets.AbstractWebSocketEndpoint;
import commonvehicle.domain.model.vehicledata.GpsCoordinates;
import commonvehicle.domain.model.vehicledata.Velocity;
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
        sendTo(deviceId, gps, "gps");
    }

    @Override
    public void onVelocityMessage(UUID deviceId) {
        Velocity velocity = speedAssistantService.getLatestVelocity(deviceId);
        sendTo(deviceId, velocity, "velocity");
    }

}
