package simulation.hardware.sensors;

import common.data.mqtt.topics.VariableMqttTopic;
import common.spi.IMqttService;
import simulation.hardware.AbstractDevice;
import simulation.hardware.State;

import java.util.concurrent.ThreadLocalRandom;

public class GPSSensor extends AbstractDevice {

    public GPSSensor(IMqttService client) {
        super(client);
    }

    public void call(State state) {
        client.publish(VariableMqttTopic.VEHICLE_GPS, state.getDeviceId(), toJson(generateGpsLatLon()));
    }

    private String toJson(double[] gpsLatLon) {
        return String.format("{ \"lat\": %f, \"lon\": %f }", gpsLatLon[0], gpsLatLon[1]);
    }

    private double[] generateGpsLatLon() {
        double lat = 55.3733;
        double lon = 10.4303;

        lat = ThreadLocalRandom.current().nextDouble(lat - 2, lat + 2);
        lon = ThreadLocalRandom.current().nextDouble(lon - 2, lon + 2);

        return new double[]{lat, lon};
    }
}
