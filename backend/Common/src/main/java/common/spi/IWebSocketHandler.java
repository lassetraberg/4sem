package common.spi;

import io.javalin.security.Role;
import io.javalin.websocket.WsSession;

import java.util.Set;

public interface IWebSocketHandler {
    String getPath();

    Set<Role> getPermittedRoles();

    void onConnect(WsSession session);

    void onMessage(WsSession session, String message);

    void onClose(WsSession session, int statusCode, String reason);

    void onError(WsSession session, Throwable throwable);
}
