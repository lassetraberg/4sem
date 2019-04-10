package simulation.hardware.sensors;

import common.data.mqtt.topics.VariableMqttTopic;
import common.spi.IMqttService;
import simulation.hardware.AbstractDevice;
import simulation.hardware.State;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class GPSSensor extends AbstractDevice {

    private List<Double[]> fileData;

    private int dataIndex = 0;
    private boolean reverse = false;

    public enum Route {
        SDU_Munkebjergvej_Motorvej_SDU("1.txt"), SDU_OdenseNord_SDU("2.txt"), SDU_Nyborg("3.txt"), Random(null);

        private String file;
        Route(String s) {
            this.file = s;
        }
    }

    public GPSSensor(IMqttService client, Route route) {
        super(client);
        if (route.file != null) {
            this.fileData = readFileData(route.file);
        } else {
            this.fileData = null;
        }
    }

    public void call(State state) {
        if (fileData != null) {
            client.publish(VariableMqttTopic.VEHICLE_GPS, state.getDeviceId(), toJson(getPredefinedGpsLatLon()));
        } else {
            client.publish(VariableMqttTopic.VEHICLE_GPS, state.getDeviceId(), toJson(generateRandomGpsLatLon()));
        }
    }

    private String toJson(double[] gpsLatLon) {
        return String.format("{ \"lat\": %f, \"lon\": %f }", gpsLatLon[0], gpsLatLon[1]);
    }

    private double[] generateRandomGpsLatLon() {
        double lat = 55.3733;
        double lon = 10.4303;

        lat = ThreadLocalRandom.current().nextDouble(lat - 2, lat + 2);
        lon = ThreadLocalRandom.current().nextDouble(lon - 2, lon + 2);

        return new double[]{lat, lon};
    }

    private double[] getPredefinedGpsLatLon() {
        Double[] latlon = fileData.get(dataIndex);
        if (dataIndex == fileData.size() - 1) {
            reverse = true;
        } else if (dataIndex == 0) {
            reverse = false;
        }
        if (reverse) {
            dataIndex--;
        } else {
            dataIndex++;
        }

        return new double[]{latlon[0], latlon[1]};
    }

    private List<Double[]> readFileData(String gpsRouteFileName) {
        List<Double[]> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/simulation/gpsroutes/" + gpsRouteFileName), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#")) continue;
                Double[] latlon = Arrays.stream(line.split(",")).map(it -> Double.parseDouble(it.trim())).collect(Collectors.toList()).toArray(new Double[]{});
                list.add(latlon);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }
}
