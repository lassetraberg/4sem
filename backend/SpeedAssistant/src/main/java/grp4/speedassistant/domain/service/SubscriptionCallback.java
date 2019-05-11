package grp4.speedassistant.domain.service;

import grp4.common.data.mqtt.topics.StaticMqttTopic;

import java.util.UUID;

public interface SubscriptionCallback {
    void accept(UUID deviceId, StaticMqttTopic definedTopic);
}
