package speedassistant.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.data.mqtt.topics.StaticMqttTopic;
import common.spi.IMqttService;
import common.util.SPILocator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import speedassistant.domain.service.ISpeedAssistantService;
import speedassistant.domain.service.ISpeedLimitService;
import speedassistant.domain.service.SpeedAssistantService;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * This integration test requires a running MQTT server, and a simulation or a real device.
 */
public class SpeedAssistantServiceTest {
    @Mock
    private ISpeedLimitService speedLimitService;

    private ISpeedAssistantService speedAssistantService;

    private UUID deviceId = UUID.fromString("cc9d7c9b-fb0f-40d7-bd83-ba4d4e97e48b");



    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        ObjectMapper mapper = new ObjectMapper();
        IMqttService mqttService = SPILocator.locateSpecific(IMqttService.class);
        speedAssistantService = new SpeedAssistantService(mqttService, speedLimitService, mapper,
                this::subscriptionCallback, StaticMqttTopic.values());
    }

    private void subscriptionCallback(UUID deviceId, StaticMqttTopic definedTopic) { }

    @Test
    public void shouldReceiveMqttMessages() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2); // Wait for everything to connect and start receiving data from MQTT

        Assert.assertNotNull(speedAssistantService.getLatestGpsCoordinate(deviceId));
        Assert.assertNotNull(speedAssistantService.getLatestVelocity(deviceId));
    }
}
