package common.spi;

import common.data.mqtt.topics.StaticMqttTopic;
import common.data.mqtt.topics.VariableMqttTopic;

import java.util.function.BiConsumer;

public interface IMqttService {
    /**
     * Publish the message on a topic.
     *
     * @param topic   a static topic, does not take any arguments
     * @param message message payload
     */
    void publish(StaticMqttTopic topic, String message);

    /**
     * Publish a message to a variable topic with a device id.
     *
     * @param topic    a variable topic, that takes device id as argument
     * @param deviceId the device id
     * @param message  message payload
     */
    void publish(VariableMqttTopic topic, String deviceId, String message);

    /**
     * Subscribes to messages on a topic.
     *
     * @param topic    a static topic, does not take any arguments
     * @param callback callback that takes two arguments: (topic, message)
     */
    void subscribe(StaticMqttTopic topic, BiConsumer<StaticMqttTopic, String> callback);

    /**
     * Subscribe to messages on a variable topic, for a certian device.
     *
     * @param topic    a topic
     * @param deviceId which device
     * @param callback callback that takes two arguments: (topic, message)
     */
    void subscribe(VariableMqttTopic topic, String deviceId, BiConsumer<VariableMqttTopic, String> callback);

    void disconnect();
}
