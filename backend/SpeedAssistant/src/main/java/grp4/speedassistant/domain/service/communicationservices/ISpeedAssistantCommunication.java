package grp4.speedassistant.domain.service.communicationservices;

import java.util.UUID;

public interface ISpeedAssistantCommunication {
    void onGpsMessage(UUID deviceId);

    void onVelocityMessage(UUID deviceId);

    void onAccelerationMessage(UUID deviceId);

}
