package speedassistant.domain.service.communicationservices;


import speedassistant.domain.service.ISpeedAssistantService;

import java.util.UUID;

public class MqttCommunicationService implements ISpeedAssistantCommunication {

    private ISpeedAssistantService speedAssistantService;

    public MqttCommunicationService(ISpeedAssistantService speedAssistantService) {
        this.speedAssistantService = speedAssistantService;
    }

    public void onGpsMessage(UUID deviceId) {
    }

    public void onVelocityMessage(UUID deviceId) {
        if (speedAssistantService.isSpeeding(deviceId)) {
            speedAssistantService.publishSpeedingAlarm(deviceId);
        }
    }
}
