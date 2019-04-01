package speedassistant.domain.service;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.data.mqtt.topics.VariableMqttTopic;
import common.spi.IMqttService;
import speedassistant.domain.GpsCoordinates;
import speedassistant.domain.Velocity;

import java.io.IOException;
import java.util.UUID;

public class SpeedAssistantMqttCommunicationService implements ISpeedAssistantCommunication{
    private ISpeedLimitService speedLimitService;
    private IMqttService mqttService;
    private ObjectMapper mapper;

    private GpsCoordinates lastSeenGpsCoord;

    public SpeedAssistantMqttCommunicationService(ISpeedLimitService speedLimitService, IMqttService mqttService, ObjectMapper mapper) {
        this.speedLimitService = speedLimitService;
        this.mqttService = mqttService;
        this.mapper = mapper;
    }

    public void onGpsMessage(UUID deviceId, String msg) {
        try {
            lastSeenGpsCoord = mapper.readValue(msg, GpsCoordinates.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onVelocityMessage(UUID deviceId, String msg) {
        int speedLimit = speedLimitService.getSpeedLimit(lastSeenGpsCoord);
        try {
            Velocity vel = mapper.readValue(msg, Velocity.class);
            if (vel.getVelocity() > speedLimit) {
                mqttService.publish(VariableMqttTopic.VEHICLE_ALARM_SPEEDING, deviceId, "true");
                //System.out.println("true");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
