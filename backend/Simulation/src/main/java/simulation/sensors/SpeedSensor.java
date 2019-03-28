package simulation.sensors;

import common.data.mqtt.MqttConnection;
import common.data.mqtt.MqttTopic;

import java.util.concurrent.ThreadLocalRandom;

public class SpeedSensor extends AbstractSensor{

    public SpeedSensor(MqttConnection client) {
        super(client);
    }

    @Override
    public void call() {
        client.publish(MqttTopic.VEHICLE_SPEED, toJson(generateSpeed()));
    }

    private String toJson(int speed) {
        return String.format("{ \"speed\": %d }", speed);
    }

    private int generateSpeed() {
        return ThreadLocalRandom.current().nextInt(0, 150);
    }
}
