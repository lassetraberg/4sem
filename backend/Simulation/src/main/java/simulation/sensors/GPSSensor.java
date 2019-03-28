package simulation.sensors;

import common.data.mqtt.MqttConnection;
import common.data.mqtt.MqttTopic;

import java.util.concurrent.ThreadLocalRandom;

public class GPSSensor extends AbstractSensor {

    public GPSSensor(MqttConnection client) {
        super(client);
    }

    public void call() {
        client.publish(MqttTopic.VEHICLE_GPS, toJson(generateGpsLatLon()));
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
