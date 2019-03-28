package simulation;

import common.data.mqtt.MqttConnection;
import simulation.sensors.AbstractSensor;
import simulation.sensors.GPSSensor;
import simulation.sensors.SpeedSensor;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SimulationMain {
    public static void main(String[] args) {
        MqttConnection mqttConnection = new MqttConnection();
        List<AbstractSensor> sensors = Arrays.asList(new GPSSensor(mqttConnection), new SpeedSensor(mqttConnection));
        int howLong = 1;

        for (int i = 0; i < howLong*60; i++) {
            for (AbstractSensor sensor : sensors) {
                sensor.call();
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
