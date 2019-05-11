package grp4.simulation;

import grp4.common.spi.IMqttService;
import grp4.common.util.SPILocator;
import grp4.simulation.hardware.AbstractDevice;
import grp4.simulation.hardware.State;
import grp4.simulation.hardware.actuators.SpeedingAlarmActuator;
import grp4.simulation.hardware.sensors.AccelerationSensor;
import grp4.simulation.hardware.sensors.GPSSensor;
import grp4.simulation.hardware.sensors.VelocitySensor;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class SimulationMain {
    public static void main(String[] args) {
        IMqttService mqttConnection = SPILocator.locateSpecific(IMqttService.class);
        mqttConnection.connect();
        Locale.setDefault(Locale.US);
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
        return Arrays.asList(
                new GPSSensor(mqttConnection, GPSSensor.Route.SDU_Munkebjergvej_Motorvej_SDU),
                new VelocitySensor(mqttConnection),
                new AccelerationSensor(mqttConnection)
        );
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
