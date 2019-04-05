package speedassistant.domain.service;

import common.data.mqtt.topics.StaticMqttTopic;

import java.util.UUID;

public interface SubscriptionCallback {
    void accept(UUID deviceId, StaticMqttTopic definedTopic);
}
