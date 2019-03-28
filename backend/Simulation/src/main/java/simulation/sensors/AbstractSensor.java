package simulation.sensors;

import common.data.mqtt.MqttConnection;

import java.util.Random;

public abstract class AbstractSensor {
    protected MqttConnection client;
    protected Random random;

    public AbstractSensor(MqttConnection client) {
        this.random = new Random();
        this.client = client;
    }

    public abstract void call();
}
