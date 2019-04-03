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
import io.javalin.security.Role;
import io.javalin.websocket.WsSession;
import speedassistant.domain.service.ISpeedLimitService;
import speedassistant.domain.service.SpeedLimitService;
import speedassistant.domain.service.communicationservices.DatabaseCommunicationService;
import speedassistant.domain.service.communicationservices.ISpeedAssistantCommunication;
import speedassistant.domain.service.communicationservices.MqttCommunicationService;
import speedassistant.web.SpeedAssistantWSController;

import java.util.*;

public class SpeedAssistantWebSocketProvider implements IWebSocketService {
    private IWebSocketAuthenticationService webSocketAuthenticationService;
    private IMqttService mqttService;

    private ISpeedLimitService speedLimitService;
    private IVehicleService vehicleService;
    private IVehicleRepository vehicleRepository;

    private ObjectMapper mapper;

    private List<ISpeedAssistantCommunication> communicationServices;
    private SpeedAssistantWSController wsController;

    public SpeedAssistantWebSocketProvider() {
        webSocketAuthenticationService = SPILocator.locateSpecific(IWebSocketAuthenticationService.class);
        mqttService = SPILocator.locateSpecific(IMqttService.class);
        vehicleRepository = SPILocator.locateSpecific(IVehicleRepository.class);
        vehicleService = SPILocator.locateSpecific(IVehicleService.class);
        vehicleService.setAccountRepository(SPILocator.locateSpecific(IAccountRepository.class));
        vehicleService.setVehicleRepository(vehicleRepository);


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
        wsController = new SpeedAssistantWSController();
        return Arrays.asList(
                new MqttCommunicationService(speedLimitService, mqttService, mapper),
                new DatabaseCommunicationService(vehicleRepository),
                wsController
        );
    }

    @Override
    public IWebSocketHandler getWebSocketHandler() {
        return new IWebSocketHandler() {
            @Override
            public String getPath() {
                return "/ws/speed-assistant/:device-id/:data";
            }

            @Override
            public Set<Role> getPermittedRoles() {
                return Collections.singleton(Roles.AUTHENTICATED);
            }

            @Override
            public void onConnect(WsSession session) {
                System.out.println("New connection: " + session.getRemoteAddress().toString());
            }

            @Override
            public void onMessage(WsSession session, String message) {
                if (webSocketAuthenticationService.doesUserHaveRole(getPermittedRoles(), message)) {
                    String username = webSocketAuthenticationService.getUsername(message);
                    UUID deviceId = UUID.fromString(session.pathParam("device-id"));


                    if (/*vehicleService.userOwnsVehicle(deviceId, username)*/ true) {// TODO check if user is the owner of that deviceId
                        if (!wsController.hasSession(session)) {
                            wsController.addSession(deviceId, session);
                            session.send(String.valueOf(session.hashCode()));
                            session.send("200"); // OK
                        }
                    } else {
                        session.send("404"); // Device ID for that user not found
                    }
                } else {
                    session.send("401"); // Invalid JWT token / not logged in
                }
            }

            @Override
            public void onClose(WsSession session, int statusCode, String reason) {
                wsController.removeSession(session);
                session.disconnect();
            }

            @Override
            public void onError(WsSession session, Throwable throwable) {
                throwable.printStackTrace();
            }
        };
    }
}
