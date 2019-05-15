package grp4.speedassistant.domain.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import grp4.commonvehicle.domain.model.vehicledata.GpsCoordinates;
import grp4.speedassistant.domain.models.speedlimit.SpeedLimit;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SpeedLimitService implements ISpeedLimitService {
    private Map<GpsCoordinates, Short> cache = new HashMap<>();

    private int requestAllowedEveryNSeconds; // Only allow API requests every N seconds
    private long lastRequestTime;
    private SpeedLimit lastSpeedLimit;

    /**
     * @param rateLimit How often should the service be allowed hit the external API? (rateLimit = 5: every 5 seconds)
     */
    public SpeedLimitService(int rateLimit) {
        this.requestAllowedEveryNSeconds = rateLimit;
    }

    public SpeedLimitService() {
        this(5);
    }

    @Override
    public short getSpeedLimit(GpsCoordinates gpsCoordinates) {
        if (cache.containsKey(gpsCoordinates)) {
            return cache.get(gpsCoordinates);
        }
        short returnValue = Short.MAX_VALUE;

        URL apiUrl = buildUrl(50, gpsCoordinates.getLat(), gpsCoordinates.getLon());
        SpeedLimit speedLimit = null;

        speedLimit = makeRequest(apiUrl.toString());


        if (speedLimit != null && speedLimit.getMaxSpeed() != null) {
            returnValue = speedLimit.getMaxSpeed();
        }

        cache.put(gpsCoordinates, returnValue);

        return returnValue;
    }

    private SpeedLimit makeRequest(String url) {
        double secondDifference = (System.currentTimeMillis() - lastRequestTime) / 1000.0;
        if (secondDifference >= requestAllowedEveryNSeconds) {
            lastRequestTime = System.currentTimeMillis();
            try {
                HttpResponse<SpeedLimit> speedLimitResponse = Unirest.get(url).asObject(SpeedLimit.class);
                lastSpeedLimit = speedLimitResponse.getBody();
                return lastSpeedLimit;
            } catch (UnirestException e) {
                e.printStackTrace();
                return lastSpeedLimit;
            }
        } else {
            return lastSpeedLimit;
        }
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
