package speedassistant.web;

import common.spi.IWebSocketAuthenticationService;
import common.spi.IWebSocketHandler;
import common.web.websockets.AbstractWebSocketEndpoint;
import commonvehicle.domain.service.IVehicleService;
import io.javalin.security.Role;
import io.javalin.websocket.WsSession;

import java.util.Set;
import java.util.UUID;

public class SpeedAssistantWebSocketHandler implements IWebSocketHandler {
    private String path;
    private Set<Role> permittedRoles;

    private IWebSocketAuthenticationService webSocketAuthenticationService;
    private IVehicleService vehicleService;
    private AbstractWebSocketEndpoint<UUID> webSocketEndpoint;

    public SpeedAssistantWebSocketHandler(String path, Set<Role> permittedRoles, IWebSocketAuthenticationService webSocketAuthenticationService, IVehicleService vehicleService, AbstractWebSocketEndpoint<UUID> webSocketEndpoint) {
        this.path = path;
        this.permittedRoles = permittedRoles;
        this.webSocketAuthenticationService = webSocketAuthenticationService;
        this.vehicleService = vehicleService;
        this.webSocketEndpoint = webSocketEndpoint;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public Set<Role> getPermittedRoles() {
        return permittedRoles;
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


            if (vehicleService.userOwnsVehicle(deviceId, username)) {
                if (!webSocketEndpoint.hasSession(session)) {
                    webSocketEndpoint.addSession(deviceId, session);
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
        System.out.println("Connection closed: " + session.getRemoteAddress().toString());
        webSocketEndpoint.removeSession(session);
        session.close();
        session.disconnect();
    }

    @Override
    public void onError(WsSession session, Throwable throwable) {
        throwable.printStackTrace();
    }
}
