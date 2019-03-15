package core;

import common.data.mqtt.MqttConnection;
import core.config.ConfigFactory;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

public class App {
    public static void main(String[] args) throws MqttException {
        MqttConnection connection = new MqttConnection();
        IMqttClient client = connection.getClient();
        client.subscribe("/test", (s, mqttMessage) -> {
            System.out.println(s);
            System.out.println(new String(mqttMessage.getPayload()));
        });
        //ConfigFactory configFactory = new ConfigFactory();
        //configFactory.getAppConfig().setup().start();
    }
}
