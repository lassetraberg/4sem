package speedassistant.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.data.mqtt.topics.StaticMqttTopic;
import common.data.mqtt.topics.VariableMqttTopic;
import common.spi.IMqttService;
import common.util.StringUtils;
import commonvehicle.domain.model.vehicledata.Acceleration;
import commonvehicle.domain.model.vehicledata.GpsCoordinates;
import commonvehicle.domain.model.vehicledata.Vehicle;
import commonvehicle.domain.model.vehicledata.Velocity;
import speedassistant.domain.dto.MqttGpsDto;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpeedAssistantService implements ISpeedAssistantService {
    private IMqttService mqttService;
    private ISpeedLimitService speedLimitService;
    private ObjectMapper mapper;
    private SubscriptionCallback subscriptionCallback;

    private Map<UUID, Vehicle> latestValues;

    public SpeedAssistantService(IMqttService mqttService, ISpeedLimitService speedLimitService, ObjectMapper mapper, SubscriptionCallback subscriptionCallback, StaticMqttTopic... topicsToSubscribeTo) {
        this.mqttService = mqttService;
        this.speedLimitService = speedLimitService;
        this.mapper = mapper;
        this.subscriptionCallback = subscriptionCallback;

        this.latestValues = new HashMap<>();

        mqttService.connect();
        subscribe(topicsToSubscribeTo);
    }


    @Override
    public Short getLatestVelocity(UUID deviceId) {
        Short latestVelocity = latestValues.get(deviceId).getVelocity();
        return latestVelocity != null ? latestVelocity : Short.MIN_VALUE;

    }

    @Override
    public double getLatestAcceleration(UUID deviceId) {
        Double latestAccel = latestValues.get(deviceId).getAcceleration();
        return latestAccel != null ? latestAccel : Double.MIN_VALUE;
    }

    @Override
    public short getLatestSpeedLimit(UUID deviceId) {
        Short speedLimit = latestValues.get(deviceId).getSpeedLimit();
        return speedLimit != null ? speedLimit : Short.MAX_VALUE;
    }

    @Override
    public GpsCoordinates getLatestGpsCoordinate(UUID deviceId) {
        Vehicle v = latestValues.get(deviceId);
        if (v != null) {
            return v.getGpsCoordinates();
        } else {
            return null;
        }
    }

    @Override
    public Vehicle getLatestVehicleData(UUID deviceId) {
        Vehicle vehicle = latestValues.get(deviceId);
        if (vehicle != null) {
            vehicle.setDeviceId(deviceId);
        }
        // Only return when all values are not null

        if (vehicle != null && vehicle.getSpeedLimit() != null && vehicle.getVelocity() != null
                && vehicle.getGpsCoordinates() != null && vehicle.getAcceleration() != null) {
            return vehicle;
        } else {
            return null;
        }
    }

    @Override
    public boolean isSpeeding(UUID deviceId) {
        Vehicle v = latestValues.get(deviceId);
        if (v.getSpeedLimit() != null && v.getVelocity() != null) {
            return v.getVelocity() > v.getSpeedLimit();
        } else {
            return false;
        }
    }

    @Override
    public void publishSpeedingAlarm(UUID deviceId, boolean isSpeeding) {
        mqttService.publish(VariableMqttTopic.VEHICLE_ALARM_SPEEDING, deviceId, isSpeeding ? "1" : "0");
    }

    private void subscribe(StaticMqttTopic... definedTopics) {
        for (StaticMqttTopic definedTopic : definedTopics) {
            mqttService.subscribe(definedTopic, (receivedTopic, message) -> {
                try {
                    onMessage(receivedTopic, message, definedTopic);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
        }
    }

    private void onMessage(String receivedTopic, String message, StaticMqttTopic definedTopic) throws IOException {
        //System.out.println(message);
        UUID deviceId = StringUtils.getUUIDFromTopic(receivedTopic);
        switch (definedTopic) {
            case ALL_VEHICLES_GPS:
                MqttGpsDto gps = mapper.readValue(message, MqttGpsDto.class);
                if (gps.getSatellites() != 0) {
                    updateGpsAndSpeedLimit(deviceId, gps.getCoordinates());
                }
                break;
            case ALL_VEHICLES_VELOCITY:
                Velocity velocity = mapper.readValue(message, Velocity.class);
                updateVelocity(deviceId, velocity);
                break;
            case ALL_VEHICLES_ACCELERATION:
                Acceleration acceleration = mapper.readValue(message, Acceleration.class);
                updateAcceleration(deviceId, acceleration);
                break;
        }
        subscriptionCallback.accept(deviceId, definedTopic);
    }

    private void updateGpsAndSpeedLimit(UUID deviceId, GpsCoordinates gps) {
        Vehicle v = latestValues.getOrDefault(deviceId, new Vehicle());
        v.setGpsCoordinates(gps);
        v.setSpeedLimit(speedLimitService.getSpeedLimit(gps));
        latestValues.put(deviceId, v);
    }

    private void updateVelocity(UUID deviceId, Velocity velocity) {
        Vehicle v = latestValues.getOrDefault(deviceId, new Vehicle());
        v.setVelocity(velocity.getVelocity());
        latestValues.put(deviceId, v);
    }

    private void updateAcceleration(UUID deviceId, Acceleration acceleration) {
        Vehicle v = latestValues.getOrDefault(deviceId, new Vehicle());
        v.setAcceleration(acceleration.getAcceleration());
        latestValues.put(deviceId, v);
    }


}
