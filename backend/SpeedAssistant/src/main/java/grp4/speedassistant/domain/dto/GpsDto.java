package grp4.speedassistant.domain.dto;

import grp4.commonvehicle.domain.model.vehicledata.GpsCoordinates;

public class GpsDto {
    private GpsCoordinates gps;

    public GpsDto(GpsCoordinates gps) {
        this.gps = gps;
    }

    public GpsCoordinates getGps() {
        return gps;
    }

    public void setGps(GpsCoordinates gps) {
        this.gps = gps;
    }
}
