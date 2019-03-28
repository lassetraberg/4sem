package common.data.mqtt.topics;

public enum StaticMqttTopic {
    Test("test");
    private String topic;

    StaticMqttTopic(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    public static StaticMqttTopic fromString(String topic) {
        for (StaticMqttTopic value : StaticMqttTopic.values()) {
            if (value.topic.equalsIgnoreCase(topic)) {
                return value;
            }
        }
        throw new IllegalArgumentException("No constant with topic '" + topic + "' found");
    }
}
