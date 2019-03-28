package common.data.mqtt;

public enum MqttTopic {
    VEHICLE_GPS("/vehicle/gps");

    private String topic;

    MqttTopic(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    public static MqttTopic fromString(String topic) {
        for (MqttTopic value : MqttTopic.values()) {
            if (value.topic.equalsIgnoreCase(topic)) {
                return value;
            }
        }
        throw new IllegalArgumentException("No constant with topic '" + topic + "' found");
    }
}
