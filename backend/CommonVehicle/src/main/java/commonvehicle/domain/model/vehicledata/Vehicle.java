package commonvehicle.domain.model.vehicledata;

import java.time.Instant;
import java.util.UUID;

public class Vehicle {
    private UUID deviceId;
    private Velocity velocity;
    private Instant timestamp;
    private Double acceleration;
    private Short speedLimit;
    private GpsCoordinates gpsCoordinates;

    public Vehicle(UUID deviceId, Velocity velocity, Instant timestamp, Double acceleration, Short speedLimit, GpsCoordinates gpsCoordinates) {
        this.deviceId = deviceId;
        this.velocity = velocity;
        this.timestamp = timestamp;
        this.acceleration = acceleration;
        this.speedLimit = speedLimit;
        this.gpsCoordinates = gpsCoordinates;
    }

    public Vehicle() {
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public Velocity getVelocity() {
        return velocity;
    }

    public void setVelocity(Velocity velocity) {
        this.velocity = velocity;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Double acceleration) {
        this.acceleration = acceleration;
    }

    public Short getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(Short speedLimit) {
        this.speedLimit = speedLimit;
    }

    public GpsCoordinates getGpsCoordinates() {
        return gpsCoordinates;
    }

    public void setGpsCoordinates(GpsCoordinates gpsCoordinates) {
        this.gpsCoordinates = gpsCoordinates;
    }
}
