package common.data.mqtt.topics;


import common.util.StringUtils;

import java.util.UUID;

public enum VariableMqttTopic {
    VEHICLE_ALL("/vehicle/%s/#"), VEHICLE_GPS("/vehicle/%s/gps"), VEHICLE_VELOCITY("/vehicle/%s/velocity"),
    VEHICLE_ALARM_SPEEDING("/vehicle/%s/alarms/speeding"), VEHICLE_MAX_ALLOWED_VELOCITY("/vehicle/%s/maxSpeed");

    private String topic;

    VariableMqttTopic(String topic) {
        this.topic = topic;
    }

    public String format(UUID deviceId) {
        return String.format(topic, deviceId.toString());
    }

    public static VariableMqttTopic fromString(String topic) {
        String originalTopic = getOriginalTopic(topic);
        for (VariableMqttTopic value : VariableMqttTopic.values()) {
            if (value.topic.equalsIgnoreCase(originalTopic)) {
                return value;
            }
        }
        return null;
    }

    private static String getOriginalTopic(String topic) {
        String[] parts = topic.split("/");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            if (part.isEmpty()) continue;
            sb.append("/");
            if (StringUtils.isValidUUID(part)) {
                sb.append(("%s"));
            } else {
                sb.append(part);
            }
        }

        return sb.toString();
    }

}
