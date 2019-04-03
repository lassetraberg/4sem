package speedassistant.domain.models.vehicledata;

public class VehicleData {
    private GpsCoordinates gpsCoordinates;
    private Velocity velocity;

    public VehicleData(GpsCoordinates gpsCoordinates, Velocity velocity) {
        this.gpsCoordinates = gpsCoordinates;
        this.velocity = velocity;
    }

    public VehicleData() {
    }

    public GpsCoordinates getGpsCoordinates() {
        return gpsCoordinates;
    }

    public Velocity getVelocity() {
        return velocity;
    }
}
