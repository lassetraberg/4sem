package speedassistant.web;

import common.web.AbstractWebSocketController;
import io.javalin.websocket.WsSession;
import speedassistant.domain.service.communicationservices.ISpeedAssistantCommunication;

import java.util.UUID;

public class SpeedAssistantWSController extends AbstractWebSocketController implements ISpeedAssistantCommunication {

    public void onGpsMessage(UUID deviceId, String msg) {
        WsSession session = this.sessionMap.get(deviceId);

        if (session != null && session.pathParam("data").equalsIgnoreCase("gps")) {
            sendTo(deviceId, msg);
        }
    }

    public void onVelocityMessage(UUID deviceId, String msg) {
        WsSession session = this.sessionMap.get(deviceId);

        if (session != null && session.pathParam("data").equalsIgnoreCase("velocity")) {
            sendTo(deviceId, msg);
        }
    }

}
