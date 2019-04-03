package simulation;

import common.spi.IMqttService;
import common.util.SPILocator;
import simulation.hardware.AbstractDevice;
import simulation.hardware.State;
import simulation.hardware.actuators.SpeedingAlarmActuator;
import simulation.hardware.sensors.GPSSensor;
import simulation.hardware.sensors.VelocitySensor;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SimulationMain {
    public static void main(String[] args) {
        IMqttService mqttConnection = SPILocator.locateAll(IMqttService.class).get(0);
        mqttConnection.connect();
        State state = new State();
        int howLong = 10;

        List<AbstractDevice> subscribers = getSubscribers(mqttConnection);
        List<AbstractDevice> publishers = getPublishers(mqttConnection);

        for (AbstractDevice subscriber : subscribers) {
            subscriber.call(state);
        }

        for (int i = 0; i < howLong * 60; i++) {
            for (AbstractDevice publisher : publishers) {
                publisher.call(state);
            }

            sleep(1);
        }

        mqttConnection.disconnect();
    }

    private static List<AbstractDevice> getPublishers(IMqttService mqttConnection) {
        return Arrays.asList(new GPSSensor(mqttConnection, "1.txt"), new VelocitySensor(mqttConnection));
    }

    private static List<AbstractDevice> getSubscribers(IMqttService mqttConnection) {
        return Arrays.asList(new SpeedingAlarmActuator(mqttConnection));
    }

    private static void sleep(int t) {
        try {
            TimeUnit.SECONDS.sleep(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
