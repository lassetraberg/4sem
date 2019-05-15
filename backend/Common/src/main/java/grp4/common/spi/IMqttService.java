package grp4.common.spi;

import grp4.common.data.mqtt.topics.StaticMqttTopic;
import grp4.common.data.mqtt.topics.VariableMqttTopic;
import grp4.common.lambda.ThrowingBiConsumer;

import java.util.UUID;

/**
 * Remember to call connect() before using other methods
 */
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
    void publish(VariableMqttTopic topic, UUID deviceId, String message);

    /**
     * Subscribes to messages on a topic.
     *
     * @param topic    a static topic, does not take any arguments
     * @param callback callback that takes two arguments: (topic, message)
     */
    void subscribe(StaticMqttTopic topic, ThrowingBiConsumer<String, String> callback);

    /**
     * Subscribe to messages on a variable topic, for a certain device.
     *
     * @param topic    a topic
     * @param deviceId which device
     * @param callback callback that takes two arguments: (topic, message)
     */
    void subscribe(VariableMqttTopic topic, UUID deviceId, ThrowingBiConsumer<VariableMqttTopic, String> callback);

    /**
     * Unsubscribe from a topic
     *
     * @param topic    a variable topic, that takes device id as argument
     * @param deviceId the device id
     */
    void unsubscribe(VariableMqttTopic topic, UUID deviceId);


    /**
     * Unsubscribe from a topic
     *
     * @param topic a static topic, does not take any arguments
     */
    void unsubscribe(StaticMqttTopic topic);

    void connect();

    void disconnect();

    boolean isConnected();
}
