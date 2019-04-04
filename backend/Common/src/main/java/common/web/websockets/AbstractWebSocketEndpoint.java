package common.web.websockets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.domain.model.Response;
import io.javalin.websocket.WsSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractWebSocketEndpoint<T> {
    private Map<T, List<WsSession>> sessionMap = new HashMap<>();
    private String variablePath;
    private ObjectMapper mapper;

    public AbstractWebSocketEndpoint(String variablePath, ObjectMapper mapper) {
        this.variablePath = variablePath;
        this.mapper = mapper;
    }

    public AbstractWebSocketEndpoint(ObjectMapper mapper) {
        this("data", mapper);
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

    protected void sendTo(T key, String msg, String pathParam) {
        List<WsSession> sessionList = sessionMap.get(key);

        if (sessionList != null && sessionList.size() > 0) {
            for (WsSession session : sessionList) {
                if (session.pathParam(variablePath).equalsIgnoreCase(pathParam)) {
                    session.send(msg);
                }
            }
        }
    }

    public void send(Response response, WsSession session) {
        try {
            String responseSerialized = mapper.writeValueAsString(response);
            session.send(responseSerialized);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
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
