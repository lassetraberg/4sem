package grp4.common.util;

import java.util.UUID;

public class StringUtils {
    public static boolean isValidUUID(String possibleUuid) {
        boolean isValid = true;
        try {
            UUID uuid = UUID.fromString(possibleUuid);
        } catch (IllegalArgumentException ex) {
            isValid = false;
        }

        return isValid;
    }

    public static UUID getUUIDFromTopic(String mqttTopic) {
        String[] parts = mqttTopic.split("/");
        UUID uuid = null;
        for (String part : parts) {
            try {
                uuid = UUID.fromString(part);
            } catch (IllegalArgumentException ex) {
            }
        }
        return uuid;
    }
}
