package simulation;

import common.data.mqtt.MqttConnection;
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
        MqttConnection mqttConnection = new MqttConnection();
        State state = new State();
        List<AbstractDevice> publishers = Arrays.asList(new GPSSensor(mqttConnection), new VelocitySensor(mqttConnection));
        List<AbstractDevice> subscribers = Arrays.asList(new SpeedingAlarmActuator(mqttConnection));
        int howLong = 1;

        for (AbstractDevice subscriber : subscribers) {
            subscriber.call(state);
        }

        for (int i = 0; i < howLong*60; i++) {
            for (AbstractDevice publisher : publishers) {
                publisher.call(state);
            }

            sleep(1);
        }

        mqttConnection.disconnect();
    }

    private static void sleep(int t)  {
        try {
            TimeUnit.SECONDS.sleep(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
