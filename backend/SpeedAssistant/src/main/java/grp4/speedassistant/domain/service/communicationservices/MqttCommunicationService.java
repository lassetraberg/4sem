package grp4.speedassistant.domain.service.communicationservices;


import grp4.speedassistant.domain.service.ISpeedAssistantService;

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
            speedAssistantService.publishSpeedingAlarm(deviceId, true);
        } else {
            speedAssistantService.publishSpeedingAlarm(deviceId, false);
        }
    }

    @Override
    public void onAccelerationMessage(UUID deviceId) {

    }
}
