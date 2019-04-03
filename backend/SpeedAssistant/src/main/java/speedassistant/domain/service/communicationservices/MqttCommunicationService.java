package speedassistant.domain.service.communicationservices;


import com.fasterxml.jackson.databind.ObjectMapper;
import common.data.mqtt.topics.VariableMqttTopic;
import common.spi.IMqttService;
import commonvehicle.domain.model.vehicledata.GpsCoordinates;
import commonvehicle.domain.model.vehicledata.Velocity;
import speedassistant.domain.service.ISpeedLimitService;

import java.io.IOException;
import java.util.UUID;

public class MqttCommunicationService implements ISpeedAssistantCommunication {
    private ISpeedLimitService speedLimitService;
    private IMqttService mqttService;
    private ObjectMapper mapper;

    private GpsCoordinates lastSeenGpsCoord;

    public MqttCommunicationService(ISpeedLimitService speedLimitService, IMqttService mqttService, ObjectMapper mapper) {
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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
