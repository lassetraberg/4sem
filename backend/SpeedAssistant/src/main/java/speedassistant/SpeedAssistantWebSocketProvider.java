package speedassistant;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.spi.IMqttService;
import common.spi.IWebSocketAuthenticationService;
import common.spi.IWebSocketHandler;
import common.spi.IWebSocketService;
import common.util.SPILocator;
import commonAuthentication.config.authConfig.Roles;
import io.javalin.security.Role;
import io.javalin.websocket.WsSession;
import speedassistant.domain.service.ISpeedLimitService;
import speedassistant.domain.service.SpeedAssistantMqttCommunicationService;
import speedassistant.domain.service.SpeedLimitService;
import speedassistant.web.SpeedAssistantWSController;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

public class SpeedAssistantWebSocketProvider implements IWebSocketService {
    private IWebSocketAuthenticationService webSocketAuthenticationService;
    private SpeedAssistantWSController webSocketController;

    public SpeedAssistantWebSocketProvider() {
        ObjectMapper mapper = new ObjectMapper();
        webSocketAuthenticationService = SPILocator.locateSpecific(IWebSocketAuthenticationService.class);
        IMqttService mqttService = SPILocator.locateSpecific(IMqttService.class);

        ISpeedLimitService speedLimitService = new SpeedLimitService(mapper);
        SpeedAssistantMqttCommunicationService mqttCommunicationService = new SpeedAssistantMqttCommunicationService(speedLimitService, mqttService, mapper);

        webSocketController = new SpeedAssistantWSController(mqttService, mqttCommunicationService);
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
                    webSocketController.addSession(deviceId, session);
                } else {
                    System.out.println("wtf");
                }
            }

            @Override
            public void onClose(WsSession session, int statusCode, String reason) {
                webSocketController.removeSession(session);
            }

            @Override
            public void onError(WsSession session, Throwable throwable) {
                throwable.printStackTrace();
            }
        };
    }
}
