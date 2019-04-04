package speedassistant.domain.models.vehicledata;

import java.time.Instant;

public class Vehicle {
    private Velocity velocity;
    private Instant timestamp;
    private Double acceleration;
    private Integer speedLimit;
    private GpsCoordinates gpsCoordinates;

    public Vehicle(Velocity velocity, Instant timestamp, Double acceleration, Integer speedLimit, GpsCoordinates gpsCoordinates) {
        this.velocity = velocity;
        this.timestamp = timestamp;
        this.acceleration = acceleration;
        this.speedLimit = speedLimit;
        this.gpsCoordinates = gpsCoordinates;
    }

    public Vehicle() {
    }

    public Velocity getVelocity() {
        return velocity;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Double getAcceleration() {
        return acceleration;
    }

    public Integer getSpeedLimit() {
        return speedLimit;
    }

    public GpsCoordinates getGpsCoordinates() {
        return gpsCoordinates;
    }

    public void setVelocity(Velocity velocity) {
        this.velocity = velocity;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public void setAcceleration(Double acceleration) {
        this.acceleration = acceleration;
    }

    public void setSpeedLimit(Integer speedLimit) {
        this.speedLimit = speedLimit;
    }

    public void setGpsCoordinates(GpsCoordinates gpsCoordinates) {
        this.gpsCoordinates = gpsCoordinates;
    }
}
