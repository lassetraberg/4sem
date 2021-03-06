package grp4.simulation.hardware;

import grp4.common.spi.IMqttService;

import java.util.Random;

public abstract class AbstractDevice {
    protected IMqttService client;
    protected Random random;

    public AbstractDevice(IMqttService client) {
        this.random = new Random();
        this.client = client;
    }

    public abstract void call(State state);

}
