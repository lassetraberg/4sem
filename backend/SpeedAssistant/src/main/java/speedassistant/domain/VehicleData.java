package speedassistant.domain;

import speedassistant.domain.GpsCoordinates;
import speedassistant.domain.Velocity;

public class VehicleData {
    private GpsCoordinates gpsCoordinates;
    private Velocity velocity;

    public VehicleData(GpsCoordinates gpsCoordinates, Velocity velocity) {
        this.gpsCoordinates = gpsCoordinates;
        this.velocity = velocity;
    }

    public VehicleData() { }

    public GpsCoordinates getGpsCoordinates() {
        return gpsCoordinates;
    }

    public Velocity getVelocity() {
        return velocity;
    }
}
