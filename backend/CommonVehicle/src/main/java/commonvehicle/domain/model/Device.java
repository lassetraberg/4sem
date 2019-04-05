package commonvehicle.domain.model;

import java.time.Instant;
import java.util.UUID;

public class Device {
    private UUID deviceId;
    private Instant lastActive;
    private String licensePlate;

    public Device(UUID deviceId, Instant lastActive, String licensePlate) {
        this.deviceId = deviceId;
        this.lastActive = lastActive;
        this.licensePlate = licensePlate;
    }

    public Device() {
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public Instant getLastActive() {
        return lastActive;
    }

    public String getLicensePlate() {
        return licensePlate;
    }
}
