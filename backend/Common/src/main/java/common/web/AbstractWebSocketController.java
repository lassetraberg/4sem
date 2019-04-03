package common.web;

import io.javalin.websocket.WsSession;

import java.util.*;

public abstract class AbstractWebSocketController {
    protected Map<UUID, WsSession> sessionMap = new HashMap<>();

    public void addSession(UUID deviceId, WsSession session) {
        sessionMap.put(deviceId, session);
    }

    public void removeSession(WsSession session) {
        UUID deviceId = null;
        for (Map.Entry<UUID, WsSession> entry : sessionMap.entrySet()) {
            if (entry.getValue().equals(session)) {
                deviceId = entry.getKey();
                break;
            }
        }

        sessionMap.remove(deviceId);
    }

    protected void broadcast(String msg) {
        sessionMap.values().stream()
                .filter(WsSession::isOpen)
                .forEach(s -> s.send(msg));
    }

    protected void sendTo(UUID deviceId, String msg) {
        sessionMap.entrySet().stream()
                .filter(entry -> entry.getKey().equals(deviceId) && entry.getValue().isOpen())
                .forEach(entry -> entry.getValue().send(msg));
    }

}
