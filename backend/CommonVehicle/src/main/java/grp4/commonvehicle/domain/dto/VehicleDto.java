package grp4.commonvehicle.domain.dto;

import grp4.commonvehicle.domain.model.vehicledata.GpsCoordinates;

import java.util.UUID;

public class VehicleDto {
    private UUID deviceId;
    private short speed;
    private double acceleration;
    private short speedLimit;
    private GpsCoordinates gpsCoordinates;

    public VehicleDto(UUID deviceId, short speed, double acceleration, short speedLimit, GpsCoordinates gpsCoordinates) {
        this.deviceId = deviceId;
        this.speed = speed;
        this.acceleration = acceleration;
        this.speedLimit = speedLimit;
        this.gpsCoordinates = gpsCoordinates;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public short getSpeed() {
        return speed;
    }

    public void setSpeed(short speed) {
        this.speed = speed;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }

    public short getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(short speedLimit) {
        this.speedLimit = speedLimit;
    }

    public GpsCoordinates getGpsCoordinates() {
        return gpsCoordinates;
    }

    public void setGpsCoordinates(GpsCoordinates gpsCoordinates) {
        this.gpsCoordinates = gpsCoordinates;
    }
}
