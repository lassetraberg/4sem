package common.data.mqtt.topics;


public enum StaticMqttTopic {
    ALL_VEHICLES_GPS("/vehicle/+/gps"), ALL_VEHICLES_VELOCITY("/vehicle/+/velocity"),
    ALL_VEHICLES_ACCELERATION("/vehicle/+/acceleration");

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
        return null;
    }
}
