package simulation.hardware;

import common.data.mqtt.MqttConnection;

import java.util.Random;

public abstract class AbstractDevice {
    protected MqttConnection client;
    protected Random random;

    public AbstractDevice(MqttConnection client) {
        this.random = new Random();
        this.client = client;
    }

    public abstract void call(State state);

}
