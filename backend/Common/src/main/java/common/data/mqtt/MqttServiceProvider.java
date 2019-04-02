package common.data.mqtt;

import common.config.Config;
import common.data.mqtt.topics.StaticMqttTopic;
import common.data.mqtt.topics.VariableMqttTopic;
import common.function.ThrowingBiConsumer;
import common.spi.IMqttService;
import org.eclipse.paho.client.mqttv3.*;

import java.util.UUID;

public class MqttServiceProvider implements IMqttService {
    private IMqttClient client;
    private String alternativeUsername = null;

    public void publish(StaticMqttTopic topic, String message) {
        this.realPublish(topic.getTopic(), message);
    }

    public void publish(VariableMqttTopic topic, UUID deviceId, String message) {
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

    public void subscribe(StaticMqttTopic topic, ThrowingBiConsumer<String, String> callback) {
        this.realSubscribe(topic.getTopic(), callback);
    }

    public void subscribe(VariableMqttTopic topic, UUID deviceId, ThrowingBiConsumer<VariableMqttTopic, String> callback) {
        this.realSubscribe(topic.format(deviceId), (returnTopic, msg) ->
                callback.accept(VariableMqttTopic.fromString(returnTopic), msg));
    }

    private void realSubscribe(String topic, ThrowingBiConsumer<String, String> callback) {
        try {
            client.subscribe(topic, (mqttTopic, msg) -> {
                callback.accept(mqttTopic, new String(msg.getPayload()));
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void unsubscribe(VariableMqttTopic topic, UUID deviceId) {
        this.realUnsubscribe(topic.format(deviceId));
    }

    @Override
    public void unsubscribe(StaticMqttTopic topic) {
        this.realUnsubscribe(topic.getTopic());
    }

    private void realUnsubscribe(String topic) {
        try {
            client.unsubscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        String clientId = UUID.randomUUID().toString();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        if (alternativeUsername == null) {
            options.setUserName(Config.getInstance().getProperty("mqtt.username"));
        } else {
            options.setUserName(alternativeUsername);
        }
        options.setPassword(Config.getInstance().getProperty("mqtt.password").toCharArray());
        try {
            client = new MqttClient(Config.getInstance().getProperty("mqtt.url"), ".mqtt-client-" + clientId);
            client.connect(options);
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


}
