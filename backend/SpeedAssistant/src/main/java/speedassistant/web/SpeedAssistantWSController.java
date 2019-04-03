package speedassistant.web;

import common.web.AbstractWebSocketController;
import io.javalin.websocket.WsSession;
import speedassistant.domain.service.communicationservices.ISpeedAssistantCommunication;

import java.util.List;
import java.util.UUID;

public class SpeedAssistantWSController extends AbstractWebSocketController implements ISpeedAssistantCommunication {

    public void onGpsMessage(UUID deviceId, String msg) {
        sendTo(deviceId, msg, "gps");
    }

    public void onVelocityMessage(UUID deviceId, String msg) {
        sendTo(deviceId, msg, "velocity");
    }

}
