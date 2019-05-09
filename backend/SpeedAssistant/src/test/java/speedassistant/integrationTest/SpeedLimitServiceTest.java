package speedassistant.integrationTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import commonvehicle.domain.model.vehicledata.GpsCoordinates;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import speedassistant.domain.service.ISpeedLimitService;
import speedassistant.domain.service.SpeedLimitService;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class SpeedLimitServiceTest {

    private ISpeedLimitService speedLimitService;

    private int rateLimit = 3;

    @Before
    public void setUp() {
        initObjectMapper();

        speedLimitService = new SpeedLimitService(rateLimit);
    }

    @Test
    public void getSpeedLimit_FastRequests_ShouldGetRateLimited() {
        GpsCoordinates limit60Coordinate = new GpsCoordinates(55.375049, 10.425339); // Niels Bohrs Alle, near Campusvej. Limit should be 60
        short speedLimit60 = speedLimitService.getSpeedLimit(limit60Coordinate);

        GpsCoordinates limit110Coordinate = new GpsCoordinates(55.353179, 10.375056); // On motorway. Limit should be 110
        short speedLimit110 = speedLimitService.getSpeedLimit(limit110Coordinate);

        Assert.assertEquals(60, speedLimit60);
        Assert.assertEquals(60, speedLimit110); // Should be 60 also, because rate limiting returns the previous value, if requests happen too quickly
    }

    @Test
    public void getSpeedLimit_SlowRequests_NoRateLimit() throws InterruptedException {
        GpsCoordinates limit60Coordinate = new GpsCoordinates(55.375049, 10.425339); // Niels Bohrs Alle, near Campusvej. Limit should be 60
        short speedLimit60 = speedLimitService.getSpeedLimit(limit60Coordinate);

        TimeUnit.SECONDS.sleep(rateLimit+1);

        GpsCoordinates limit110Coordinate = new GpsCoordinates(55.353179, 10.375056); // On motorway. Limit should be 110
        short speedLimit110 = speedLimitService.getSpeedLimit(limit110Coordinate);

        Assert.assertEquals(60, speedLimit60);
        Assert.assertEquals(110, speedLimit110);
    }

    private void initObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        Unirest.setObjectMapper(new com.mashape.unirest.http.ObjectMapper() {
            @Override
            public <T> T readValue(String value, Class<T> valueType) {
                try {
                    return mapper.readValue(value, valueType);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public String writeValue(Object value) {
                try {
                    return mapper.writeValueAsString(value);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
