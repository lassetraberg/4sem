package speedassistant.web;

import common.web.AbstractWebSocketController;
import speedassistant.domain.service.communicationservices.ISpeedAssistantCommunication;

import java.util.UUID;

public class SpeedAssistantWSController extends AbstractWebSocketController implements ISpeedAssistantCommunication {

    public void onGpsMessage(UUID deviceId, String msg) {
        sendTo(deviceId, msg, "gps");
    }

    public void onVelocityMessage(UUID deviceId, String msg) {
        sendTo(deviceId, msg, "velocity");
    }

}
