package speedassistant;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.data.mqtt.topics.StaticMqttTopic;
import common.spi.IMqttService;
import common.spi.IWebSocketAuthenticationService;
import common.spi.IWebSocketHandler;
import common.spi.IWebSocketService;
import common.util.SPILocator;
import common.util.StringUtils;
import commonAuthentication.config.authConfig.Roles;
import commonAuthentication.domain.repository.IAccountRepository;
import commonvehicle.domain.repository.IVehicleRepository;
import commonvehicle.domain.service.IVehicleService;
import speedassistant.domain.service.ISpeedLimitService;
import speedassistant.domain.service.SpeedLimitService;
import speedassistant.domain.service.communicationservices.DatabaseCommunicationService;
import speedassistant.domain.service.communicationservices.ISpeedAssistantCommunication;
import speedassistant.domain.service.communicationservices.MqttCommunicationService;
import speedassistant.domain.service.communicationservices.WebSocketCommunicationService;
import speedassistant.web.SpeedAssistantWebSocketHandler;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static common.util.JavalinUtils.roles;

public class SpeedAssistantWebSocketProvider implements IWebSocketService {
    private IMqttService mqttService;

    private ISpeedLimitService speedLimitService;

    private ObjectMapper mapper;

    private List<ISpeedAssistantCommunication> communicationServices;
    private WebSocketCommunicationService webSocketCommunicationService;

    private IWebSocketHandler webSocketHandler;

    public SpeedAssistantWebSocketProvider() {
        mqttService = SPILocator.locateSpecific(IMqttService.class);
        IWebSocketAuthenticationService webSocketAuthenticationService = SPILocator.locateSpecific(IWebSocketAuthenticationService.class);
        IVehicleRepository vehicleRepository = SPILocator.locateSpecific(IVehicleRepository.class);
        IVehicleService vehicleService = SPILocator.locateSpecific(IVehicleService.class);
        vehicleService.setAccountRepository(SPILocator.locateSpecific(IAccountRepository.class));
        vehicleService.setVehicleRepository(vehicleRepository);

        webSocketCommunicationService = new WebSocketCommunicationService("data");

        webSocketHandler = new SpeedAssistantWebSocketHandler("/ws/speed-assistant/:device-id/:data",
                roles(Roles.AUTHENTICATED), webSocketAuthenticationService, vehicleService, webSocketCommunicationService);


        mapper = new ObjectMapper();
        speedLimitService = new SpeedLimitService();


        communicationServices = setupCommunicationServices();
        mqttService.connect();
        establishSubscriptions();
    }

    private void establishSubscriptions() {
        mqttService.subscribe(StaticMqttTopic.ALL_VEHICLES_GPS, ((topic, msg) -> subscriptionCallback(topic, msg, StaticMqttTopic.ALL_VEHICLES_GPS)));
        mqttService.subscribe(StaticMqttTopic.ALL_VEHICLES_VELOCITY, ((topic, msg) -> subscriptionCallback(topic, msg, StaticMqttTopic.ALL_VEHICLES_VELOCITY)));
    }

    private void subscriptionCallback(String topic, String msg, StaticMqttTopic definedTopic) {
        System.out.println(msg);
        UUID deviceId = StringUtils.getUUIDFromTopic(topic);
        for (ISpeedAssistantCommunication communicationService : communicationServices) {
            switch (definedTopic) {
                case ALL_VEHICLES:
                    break;
                case ALL_VEHICLES_GPS:
                    communicationService.onGpsMessage(deviceId, msg);
                    break;
                case ALL_VEHICLES_VELOCITY:
                    communicationService.onVelocityMessage(deviceId, msg);
                    break;
            }
        }
    }

    private List<ISpeedAssistantCommunication> setupCommunicationServices() {
        return Arrays.asList(
                new MqttCommunicationService(speedLimitService, mqttService, mapper),
                new DatabaseCommunicationService(),
                webSocketCommunicationService
        );
    }

    @Override
    public IWebSocketHandler getWebSocketHandler() {
        return webSocketHandler;
    }
}
