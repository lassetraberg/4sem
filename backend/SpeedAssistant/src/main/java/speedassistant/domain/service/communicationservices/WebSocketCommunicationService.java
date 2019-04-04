package speedassistant.domain.service.communicationservices;

import common.web.websockets.AbstractWebSocketEndpoint;

import java.util.UUID;

public class WebSocketCommunicationService extends AbstractWebSocketEndpoint<UUID> implements ISpeedAssistantCommunication {
    public WebSocketCommunicationService(String variablePath) {
        super(variablePath);
    }

    @Override
    public void onGpsMessage(UUID deviceId, String msg) {
        sendTo(deviceId, msg, "gps");
    }

    @Override
    public void onVelocityMessage(UUID deviceId, String msg) {
        sendTo(deviceId, msg, "velocity");
    }
}
