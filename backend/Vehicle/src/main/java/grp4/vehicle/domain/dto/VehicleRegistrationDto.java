package grp4.vehicle.domain.dto;

public class VehicleRegistrationDto {
    private String deviceId;
    private String licensePlate;

    public VehicleRegistrationDto(String deviceId, String licensePlate) {
        this.deviceId = deviceId;
        this.licensePlate = licensePlate;
    }

    public VehicleRegistrationDto() {
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getLicensePlate() {
        return licensePlate;
    }
}
