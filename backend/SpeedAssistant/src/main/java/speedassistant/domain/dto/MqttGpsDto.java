package speedassistant.domain.dto;

import commonvehicle.domain.model.vehicledata.GpsCoordinates;

public class MqttGpsDto {
    private int satellites;
    private String date;
    private String time;
    private GpsCoordinates coordinates;

    public MqttGpsDto(int satellites, String date, String time, GpsCoordinates coordinates) {
        this.satellites = satellites;
        this.date = date;
        this.time = time;
        this.coordinates = coordinates;
    }

    public MqttGpsDto() {
    }

    public int getSatellites() {
        return satellites;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public GpsCoordinates getCoordinates() {
        return coordinates;
    }
}
