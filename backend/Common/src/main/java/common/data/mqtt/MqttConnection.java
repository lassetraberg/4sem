package common.data.mqtt;

import common.config.Config;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.UUID;

public class MqttConnection {
    private IMqttClient client;

    public MqttConnection() {
        String clientId = UUID.randomUUID().toString();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        options.setUserName(Config.getInstance("core").getProperty("mqtt.username"));
        options.setPassword(Config.getInstance("core").getProperty("mqtt.password").toCharArray());
        try {
            client = new MqttClient(Config.getInstance("core").getProperty("mqtt.url"), ".mqtt-client-"+clientId);
            client.connect(options);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public IMqttClient getClient() {
        return client;
    }
}
