package speedassistant.web;

import common.domain.model.Response;
import common.spi.IWebSocketAuthenticationService;
import common.spi.IWebSocketHandler;
import common.web.websockets.AbstractWebSocketEndpoint;
import commonvehicle.domain.service.IVehicleService;
import io.javalin.security.Role;
import io.javalin.websocket.WsSession;
import org.eclipse.jetty.http.HttpStatus;

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
        if (!webSocketAuthenticationService.doesUserHaveRole(getPermittedRoles(), message)) {
            webSocketEndpoint.send(new Response(HttpStatus.UNAUTHORIZED_401, "Not permitted"), session);
            return;
        }

        String username = webSocketAuthenticationService.getUsername(message);
        UUID deviceId = UUID.fromString(session.pathParam("device-id"));
        if (!vehicleService.userOwnsVehicle(deviceId, username)) {
            webSocketEndpoint.send(new Response(HttpStatus.NOT_FOUND_404, "Not Found"), session);
            return;
        }

        if (!webSocketEndpoint.hasSession(session)) {
            session.setIdleTimeout(86400000); // 24 hours in milliseconds
            webSocketEndpoint.addSession(deviceId, session);
            webSocketEndpoint.send(new Response(HttpStatus.OK_200, String.valueOf(session.hashCode())), session);
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
