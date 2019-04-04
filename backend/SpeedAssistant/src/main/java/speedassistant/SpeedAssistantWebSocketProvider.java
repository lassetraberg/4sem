package speedassistant;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.data.mqtt.topics.StaticMqttTopic;
import common.spi.IMqttService;
import common.spi.IWebSocketAuthenticationService;
import common.spi.IWebSocketHandler;
import common.spi.IWebSocketService;
import common.util.SPILocator;
import commonAuthentication.config.authConfig.Roles;
import commonAuthentication.domain.repository.IAccountRepository;
import commonvehicle.domain.repository.IVehicleRepository;
import commonvehicle.domain.service.IVehicleService;
import speedassistant.domain.service.ISpeedAssistantService;
import speedassistant.domain.service.ISpeedLimitService;
import speedassistant.domain.service.SpeedAssistantService;
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
    private IVehicleService vehicleService;
    private ISpeedAssistantService speedAssistantService;

    private List<ISpeedAssistantCommunication> communicationServices;
    private WebSocketCommunicationService webSocketCommunicationService;

    private IWebSocketHandler webSocketHandler;

    public SpeedAssistantWebSocketProvider() {
        IMqttService mqttService = SPILocator.locateSpecific(IMqttService.class);
        IWebSocketAuthenticationService webSocketAuthenticationService = SPILocator.locateSpecific(IWebSocketAuthenticationService.class);
        IVehicleRepository vehicleRepository = SPILocator.locateSpecific(IVehicleRepository.class);
        vehicleService = SPILocator.locateSpecific(IVehicleService.class);
        vehicleService.setAccountRepository(SPILocator.locateSpecific(IAccountRepository.class));
        vehicleService.setVehicleRepository(vehicleRepository);

        ObjectMapper mapper = new ObjectMapper();
        ISpeedLimitService speedLimitService = new SpeedLimitService();

        speedAssistantService = new SpeedAssistantService(mqttService, speedLimitService, mapper, this::subscriptionCallback,
                StaticMqttTopic.values());

        webSocketCommunicationService = new WebSocketCommunicationService("data", mapper, speedAssistantService);

        webSocketHandler = new SpeedAssistantWebSocketHandler("/ws/speed-assistant/:device-id/:data",
                roles(Roles.AUTHENTICATED), webSocketAuthenticationService, vehicleService, webSocketCommunicationService);


        communicationServices = setupCommunicationServices();
    }

    private void subscriptionCallback(UUID deviceId, StaticMqttTopic definedTopic) {
        for (ISpeedAssistantCommunication communicationService : communicationServices)
            switch (definedTopic) {
                case ALL_VEHICLES_GPS:
                    communicationService.onGpsMessage(deviceId);
                    break;
                case ALL_VEHICLES_VELOCITY:
                    communicationService.onVelocityMessage(deviceId);
                    break;
            }
    }

    private List<ISpeedAssistantCommunication> setupCommunicationServices() {
        return Arrays.asList(
                new MqttCommunicationService(speedAssistantService),
                new DatabaseCommunicationService(speedAssistantService, vehicleService),
                webSocketCommunicationService
        );
    }

    @Override
    public IWebSocketHandler getWebSocketHandler() {
        return webSocketHandler;
    }
}
