package common.data.mqtt;

import common.config.Config;
import org.eclipse.paho.client.mqttv3.*;

import java.util.UUID;
import java.util.function.BiConsumer;

public class MqttConnection {
    private IMqttClient client;

    public MqttConnection() {
        connect();
    }

    public void publish(MqttTopic topic, String message) {
        MqttMessage mqttMsg = new MqttMessage(message.getBytes());
        mqttMsg.setQos(0);
        mqttMsg.setRetained(true);
        try {
            client.publish(topic.getTopic(), mqttMsg);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(MqttTopic topic, BiConsumer<MqttTopic, String> callback) {
        try {
            client.subscribe(topic.getTopic(), (mqttTopic, msg) ->
                    callback.accept(MqttTopic.fromString(mqttTopic), new String(msg.getPayload())));
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void connect() {
        String clientId = UUID.randomUUID().toString();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        options.setUserName(Config.getInstance().getProperty("mqtt.username"));
        options.setPassword(Config.getInstance().getProperty("mqtt.password").toCharArray());
        try {
            client = new MqttClient(Config.getInstance().getProperty("mqtt.url"), ".mqtt-client-" + clientId);
            client.connect(options);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}
