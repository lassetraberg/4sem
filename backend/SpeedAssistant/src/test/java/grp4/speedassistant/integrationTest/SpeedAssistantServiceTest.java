package grp4.speedassistant.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import grp4.common.data.mqtt.MqttServiceProvider;
import grp4.common.data.mqtt.topics.StaticMqttTopic;
import grp4.common.spi.IMqttService;
import grp4.speedassistant.domain.service.ISpeedAssistantService;
import grp4.speedassistant.domain.service.ISpeedLimitService;
import grp4.speedassistant.domain.service.SpeedAssistantService;
import org.junit.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * This integration test requires a running MQTT server, and a grp4.simulation or a real device.
 */
public class SpeedAssistantServiceTest {
    @Mock
    private ISpeedLimitService speedLimitService;

    private ISpeedAssistantService speedAssistantService;

    private UUID deviceId = UUID.fromString("cc9d7c9b-fb0f-40d7-bd83-ba4d4e97e48b");

    @BeforeClass
    public static void checkMqttConnection() {
        IMqttService mqttService = new MqttServiceProvider();
        try {
            mqttService.connect();
        } catch (RuntimeException e) {
            // do nothing
        }
        Assume.assumeTrue(mqttService.isConnected());
    }


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        ObjectMapper mapper = new ObjectMapper();
        IMqttService mqttService = new MqttServiceProvider();
        speedAssistantService = new SpeedAssistantService(mqttService, speedLimitService, mapper,
                this::subscriptionCallback, StaticMqttTopic.values());
    }

    private void subscriptionCallback(UUID deviceId, StaticMqttTopic definedTopic) {
    }

    @Test
    public void shouldReceiveMqttMessages() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2); // Wait for everything to connect and start receiving data from MQTT

        Assert.assertNotNull(speedAssistantService.getLatestGpsCoordinate(deviceId));
        Assert.assertNotNull(speedAssistantService.getLatestVelocity(deviceId));
    }
}
