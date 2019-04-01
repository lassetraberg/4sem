package speedassistant.domain.service;

import java.util.UUID;

public interface ISpeedAssistantCommunication {
    void onGpsMessage(UUID deviceId, String msg);

    void onVelocityMessage(UUID deviceId, String msg);

}
