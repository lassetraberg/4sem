package grp4.common.data.mqtt.topics;


public enum StaticMqttTopic {
    ALL_VEHICLES_GPS("/grp4.vehicle/+/gps"), ALL_VEHICLES_VELOCITY("/grp4.vehicle/+/velocity"),
    ALL_VEHICLES_ACCELERATION("/grp4.vehicle/+/acceleration");

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
