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
    public Short getLatestVelocity(UUID vehicleId) {
        return latestValues.get(vehicleId).getVelocity();
    }

    @Override
    public double getLatestAcceleration(UUID vehicleId) {
        return latestValues.get(vehicleId).getAcceleration();
    }

    @Override
    public short getLatestSpeedLimit(UUID vehicleId) {
        return latestValues.get(vehicleId).getSpeedLimit();
    }

    @Override
    public GpsCoordinates getLatestGpsCoordinate(UUID vehicleId) {
        return latestValues.get(vehicleId).getGpsCoordinates();
    }

    @Override
    public Vehicle getLatestVehicleData(UUID vehicleId) {
        Vehicle vehicle = latestValues.get(vehicleId);
        vehicle.setDeviceId(vehicleId);
        // Only return when all values are not null

        if (vehicle.getSpeedLimit() != null && vehicle.getVelocity() != null
                && vehicle.getGpsCoordinates() != null && vehicle.getAcceleration() != null) {
            return vehicle;
        } else {
            return null;
        }
    }

    @Override
    public boolean isSpeeding(UUID vehicleId) {
        Vehicle v = latestValues.get(vehicleId);
        if (v.getSpeedLimit() != null && v.getVelocity() != null) {
            return v.getVelocity() > v.getSpeedLimit();
        } else {
            return false;
        }
    }

    @Override
    public void publishSpeedingAlarm(UUID vehicleId) {
        mqttService.publish(VariableMqttTopic.VEHICLE_ALARM_SPEEDING, vehicleId, "1");
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
        System.out.println(message);
        UUID vehicleId = StringUtils.getUUIDFromTopic(receivedTopic);
        switch (definedTopic) {
            case ALL_VEHICLES_GPS:
                GpsCoordinates gps = mapper.readValue(message, GpsCoordinates.class);
                updateGpsAndSpeedLimit(vehicleId, gps);
                break;
            case ALL_VEHICLES_VELOCITY:
                Velocity velocity = mapper.readValue(message, Velocity.class);
                updateVelocity(vehicleId, velocity);
                break;
            case ALL_VEHICLES_ACCELERATION:
                Acceleration acceleration = mapper.readValue(message, Acceleration.class);
                updateAcceleration(vehicleId, acceleration);
                break;
        }
        subscriptionCallback.accept(vehicleId, definedTopic);
    }

    private void updateGpsAndSpeedLimit(UUID vehicleId, GpsCoordinates gps) {
        Vehicle v = latestValues.getOrDefault(vehicleId, new Vehicle());
        v.setGpsCoordinates(gps);
        v.setSpeedLimit(speedLimitService.getSpeedLimit(gps));
        latestValues.put(vehicleId, v);
    }

    private void updateVelocity(UUID vehicleId, Velocity velocity) {
        Vehicle v = latestValues.getOrDefault(vehicleId, new Vehicle());
        v.setVelocity(velocity.getVelocity());
        latestValues.put(vehicleId, v);
    }

    private void updateAcceleration(UUID vehicleId, Acceleration acceleration) {
        Vehicle v = latestValues.getOrDefault(vehicleId, new Vehicle());
        v.setAcceleration(acceleration.getAcceleration());
        latestValues.put(vehicleId, v);
    }


}
