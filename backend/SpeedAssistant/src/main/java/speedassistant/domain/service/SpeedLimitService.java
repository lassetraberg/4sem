package speedassistant.domain.service;

import commonvehicle.domain.model.vehicledata.GpsCoordinates;
import speedassistant.domain.models.speedlimit.SpeedLimit;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SpeedLimitService implements ISpeedLimitService {
    private Map<GpsCoordinates, Short> cache = new HashMap<>();

    public SpeedLimitService() {
    }


    @Override
    public short getSpeedLimit(GpsCoordinates gpsCoordinates) {
        if (cache.containsKey(gpsCoordinates)) {
            return cache.get(gpsCoordinates);
        }
        short returnValue = Short.MAX_VALUE;

        URL apiUrl = buildUrl(50, gpsCoordinates.getLat(), gpsCoordinates.getLon());
        SpeedLimit speedLimit = null;

        //try {
        //    HttpResponse<SpeedLimit> speedLimitResponse = Unirest.get(apiUrl.toString()).asObject(SpeedLimit.class);
        //    speedLimit = speedLimitResponse.getBody();
        //} catch (UnirestException e) {
        //    e.printStackTrace();
        //}

        if (speedLimit != null && speedLimit.getMaxSpeed() != null) {
            returnValue = speedLimit.getMaxSpeed();
        }

        cache.put(gpsCoordinates, returnValue);


        return returnValue;
    }


    private URL buildUrl(double radius, double lat, double lon) {
        String baseApiUrl = "https://overpass-api.de/api/interpreter?data=[out:json];way[maxspeed](around:%f,%f,%f);out%%20tags;"; // Double %% to escape string format

        String formattedApiUrl = String.format(baseApiUrl, radius, lat, lon);

        try {
            return new URL(formattedApiUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
