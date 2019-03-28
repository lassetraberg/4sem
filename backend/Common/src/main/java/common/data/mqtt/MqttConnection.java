package common.data.mqtt;

import common.config.Config;
import common.data.mqtt.topics.StaticMqttTopic;
import common.data.mqtt.topics.VariableMqttTopic;
import org.eclipse.paho.client.mqttv3.*;

import java.util.UUID;
import java.util.function.BiConsumer;

public class MqttConnection {
    private IMqttClient client;

    public MqttConnection() {
        connect();
    }

    public void publish(StaticMqttTopic topic, String message) {
        this.realPublish(topic.getTopic(), message);
    }

    public void publish(VariableMqttTopic topic, String deviceId, String message) {
        this.realPublish(topic.format(deviceId), message);
    }

    private void realPublish(String topic, String message) {
        MqttMessage mqttMsg = new MqttMessage(message.getBytes());
        mqttMsg.setQos(0);
        mqttMsg.setRetained(true);
        try {
            client.publish(topic, mqttMsg);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(StaticMqttTopic topic, BiConsumer<StaticMqttTopic, String> callback) {
        this.realSubscribe(topic.getTopic(), (returnTopic, msg) -> {
            callback.accept(StaticMqttTopic.fromString(returnTopic), msg);
        });
    }

    public void subscribe(VariableMqttTopic topic, String deviceId, BiConsumer<VariableMqttTopic, String> callback) {
        this.realSubscribe(topic.format(deviceId), (returnTopic, msg) -> {
            callback.accept(VariableMqttTopic.fromString(returnTopic), msg);
        });
    }

    private void realSubscribe(String topic, BiConsumer<String, String> callback) {
        try {
            client.subscribe(topic, (mqttTopic, msg) -> {
                callback.accept(mqttTopic, new String(msg.getPayload()));
            });
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
