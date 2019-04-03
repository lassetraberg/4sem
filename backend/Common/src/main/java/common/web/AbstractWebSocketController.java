package common.web;

import io.javalin.websocket.WsSession;

import java.util.*;

public abstract class AbstractWebSocketController {
    protected Map<UUID, List<WsSession>> sessionMap = new HashMap<>();

    public void addSession(UUID deviceId, WsSession session) {
        if (sessionMap.containsKey(deviceId)) {
            List<WsSession> sessionList = sessionMap.get(deviceId);
            sessionList.add(session);
            sessionMap.put(deviceId, sessionList);
        } else {
            List<WsSession> sessionList = new ArrayList<>();
            sessionList.add(session);
            sessionMap.put(deviceId, sessionList);
        }
    }

    public void removeSession(WsSession session) {
        UUID deviceId = null;
        for (Map.Entry<UUID, List<WsSession>> entry : sessionMap.entrySet()) {
            for (WsSession s : entry.getValue()) {
                if (s.equals(session)) {
                    deviceId = entry.getKey();
                    break;
                }
            }
        }

        List<WsSession> sessionList = sessionMap.get(deviceId);
        if (sessionList != null) {
            sessionList.remove(session);
            sessionMap.put(deviceId, sessionList);
        }
    }


    protected void broadcast(String msg) {
        sessionMap.values().stream()
                .flatMap(List::stream)
                .filter(WsSession::isOpen)
                .forEach(s -> s.send(msg));
    }

    protected void sendTo(UUID deviceId, String msg, String data) {
        List<WsSession> sessionList = sessionMap.get(deviceId);

        if (sessionList != null && sessionList.size() > 0) {
            for (WsSession session : sessionList) {
                if (session.pathParam("data").equalsIgnoreCase(data)) {
                    session.send(msg);
                }
            }
        }
    }


}
