package common.web.websockets;

import io.javalin.websocket.WsSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractWebSocketEndpoint<T> {
    private Map<T, List<WsSession>> sessionMap = new HashMap<>();
    private String variablePath;

    public AbstractWebSocketEndpoint(String variablePath) {
        this.variablePath = variablePath;
    }

    public AbstractWebSocketEndpoint() {
        this("data");
    }

    public void addSession(T key, WsSession session) {
        if (sessionMap.containsKey(key)) {
            List<WsSession> sessionList = sessionMap.get(key);
            sessionList.add(session);
            sessionMap.put(key, sessionList);
        } else {
            List<WsSession> sessionList = new ArrayList<>();
            sessionList.add(session);
            sessionMap.put(key, sessionList);
        }
    }

    public void removeSession(WsSession session) {
        T key = null;
        for (Map.Entry<T, List<WsSession>> entry : sessionMap.entrySet()) {
            for (WsSession s : entry.getValue()) {
                if (s.equals(session)) {
                    key = entry.getKey();
                    break;
                }
            }
        }

        List<WsSession> sessionList = sessionMap.get(key);
        if (sessionList != null) {
            sessionList.remove(session);
            sessionMap.put(key, sessionList);
        }
    }


    protected void broadcast(String msg) {
        sessionMap.values().stream()
                .flatMap(List::stream)
                .filter(WsSession::isOpen)
                .forEach(s -> s.send(msg));
    }

    protected void sendTo(T key, String msg, String data) {
        List<WsSession> sessionList = sessionMap.get(key);

        if (sessionList != null && sessionList.size() > 0) {
            for (WsSession session : sessionList) {
                if (session.pathParam(variablePath).equalsIgnoreCase(data)) {
                    session.send(msg);
                }
            }
        }
    }

    public boolean hasSession(WsSession session) {
        for (List<WsSession> value : sessionMap.values()) {
            for (WsSession wsSession : value) {
                if (wsSession.equals(session)) {
                    return true;
                }
            }
        }
        return false;
    }


}
