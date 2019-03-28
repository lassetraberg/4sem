package common.data.mqtt;

public enum MqttTopic {
    VEHICLE_ALL("/%s/vehicle/#"), VEHICLE_GPS("/%s/vehicle/gps"), VEHICLE_VELOCITY("/%s/vehicle/speed"),
    VEHICLE_ALARM_SPEEDING("/%s/vehicle/alarms/speeding"), VEHICLE_MAX_ALLOWED_VELOCITY("/%s/vehicle/maxSpeed");

    private String topic;

    MqttTopic(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    public String format(String deviceId) {
        return String.format(topic, deviceId);
    }

    public static MqttTopic fromString(String topic) {
        String originalTopic = getOriginalTopic(topic);
        for (MqttTopic value : MqttTopic.values()) {
            if (value.topic.equalsIgnoreCase(originalTopic)) {
                return value;
            }
        }
        throw new IllegalArgumentException("No constant with topic '" + topic + "' found");
    }

    private static String getOriginalTopic(String topic) {
        String[] parts = topic.split("/");
        StringBuilder sb = new StringBuilder("/%s");
        if (parts.length > 3) {
            for (int i = 2; i < parts.length; i++) {
                sb.append("/").append(parts[i]);
            }
        }

        return sb.toString();
    }
}
