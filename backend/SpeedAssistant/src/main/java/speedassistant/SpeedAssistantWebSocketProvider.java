package speedassistant;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import common.data.mqtt.topics.StaticMqttTopic;
import common.spi.IMqttService;
import common.spi.IWebSocketAuthenticationService;
import common.spi.IWebSocketHandler;
import common.spi.IWebSocketService;
import common.util.SPILocator;
import common.util.StringUtils;
import commonAuthentication.config.authConfig.Roles;
import io.javalin.security.Role;
import io.javalin.websocket.WsSession;
import speedassistant.domain.repository.IVehicleRepository;
import speedassistant.domain.service.ISpeedLimitService;
import speedassistant.domain.service.SpeedLimitService;
import speedassistant.domain.service.communicationservices.DatabaseCommunicationService;
import speedassistant.domain.service.communicationservices.ISpeedAssistantCommunication;
import speedassistant.domain.service.communicationservices.MqttCommunicationService;
import speedassistant.web.SpeedAssistantWSController;

import java.io.IOException;
import java.util.*;

public class SpeedAssistantWebSocketProvider implements IWebSocketService {
    private ObjectMapper mapper;
    private IWebSocketAuthenticationService webSocketAuthenticationService;
    private IMqttService mqttService;
    private ISpeedLimitService speedLimitService;

    private IVehicleRepository vehicleRepository;

    private List<ISpeedAssistantCommunication> communicationServices;
    private SpeedAssistantWSController wsController;

    public SpeedAssistantWebSocketProvider() {
        initObjectMapper();
        webSocketAuthenticationService = SPILocator.locateSpecific(IWebSocketAuthenticationService.class);
        mqttService = SPILocator.locateSpecific(IMqttService.class);
        speedLimitService = new SpeedLimitService();


        communicationServices = setupCommunicationServices();
        mqttService.connect();
        establishSubscriptions();
    }

    private void establishSubscriptions() {
        mqttService.subscribe(StaticMqttTopic.ALL_VEHICLES_GPS, ((topic, msg) -> this.subscriptionCallback(topic, msg, StaticMqttTopic.ALL_VEHICLES_GPS)));
        mqttService.subscribe(StaticMqttTopic.ALL_VEHICLES_VELOCITY, (topic, msg) -> subscriptionCallback(topic, msg, StaticMqttTopic.ALL_VEHICLES_VELOCITY));
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

            }

            @Override
            public void onMessage(WsSession session, String message) {
                if (webSocketAuthenticationService.doesUserHaveRole(getPermittedRoles(), message)) {
                    String username = webSocketAuthenticationService.getUsername(message);
                    UUID deviceId = UUID.fromString(session.pathParam("device-id"));

                    // TODO check if user is the owner of that deviceId
                    wsController.addSession(deviceId, session);
                } else {
                    System.out.println("wtf");
                }
            }

            @Override
            public void onClose(WsSession session, int statusCode, String reason) {
                wsController.removeSession(session);
            }

            @Override
            public void onError(WsSession session, Throwable throwable) {
                throwable.printStackTrace();
            }
        };
    }

    private void initObjectMapper() {
        mapper = new ObjectMapper();
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
