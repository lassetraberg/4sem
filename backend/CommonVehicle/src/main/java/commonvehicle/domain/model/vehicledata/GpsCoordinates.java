package commonvehicle.domain.model.vehicledata;

import java.util.Objects;

public class GpsCoordinates {
    private double lat;
    private double lon;

    public GpsCoordinates(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public GpsCoordinates() {
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "GpsCoordinates{" +
                "lat=" + lat +
                ", lon=" + lon +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GpsCoordinates that = (GpsCoordinates) o;
        return Double.compare(that.lat, lat) == 0 &&
                Double.compare(that.lon, lon) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lat, lon);
    }
}
