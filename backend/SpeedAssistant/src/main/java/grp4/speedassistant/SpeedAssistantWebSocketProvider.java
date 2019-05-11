package grp4.speedassistant;

import com.fasterxml.jackson.databind.ObjectMapper;
import grp4.common.data.mqtt.topics.StaticMqttTopic;
import grp4.common.spi.IMqttService;
import grp4.common.spi.IWebSocketAuthenticationService;
import grp4.common.spi.IWebSocketHandler;
import grp4.common.spi.IWebSocketService;
import grp4.common.util.SPILocator;
import grp4.commonAuthentication.config.authConfig.Role;
import grp4.commonAuthentication.domain.repository.IAccountRepository;
import grp4.commonvehicle.domain.repository.IVehicleRepository;
import grp4.commonvehicle.domain.service.IVehicleService;
import grp4.speedassistant.domain.service.ISpeedAssistantService;
import grp4.speedassistant.domain.service.ISpeedLimitService;
import grp4.speedassistant.domain.service.SpeedAssistantService;
import grp4.speedassistant.domain.service.SpeedLimitService;
import grp4.speedassistant.domain.service.communicationservices.DatabaseCommunicationService;
import grp4.speedassistant.domain.service.communicationservices.ISpeedAssistantCommunication;
import grp4.speedassistant.domain.service.communicationservices.MqttCommunicationService;
import grp4.speedassistant.domain.service.communicationservices.WebSocketCommunicationService;
import grp4.speedassistant.web.SpeedAssistantWebSocketHandler;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static grp4.common.util.JavalinUtils.roles;

@Service
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
        ISpeedLimitService speedLimitService = new SpeedLimitService(10);

        speedAssistantService = new SpeedAssistantService(mqttService, speedLimitService, mapper, this::subscriptionCallback,
                StaticMqttTopic.values());

        webSocketCommunicationService = new WebSocketCommunicationService("data", mapper, speedAssistantService);

        webSocketHandler = new SpeedAssistantWebSocketHandler("/ws/speed-assistant/:device-id/:data",
                roles(Role.AUTHENTICATED), webSocketAuthenticationService, vehicleService, webSocketCommunicationService);


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
                case ALL_VEHICLES_ACCELERATION:
                    communicationService.onAccelerationMessage(deviceId);
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
