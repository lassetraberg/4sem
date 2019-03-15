package common.spi;

import io.javalin.websocket.WsSession;

public interface IWebSocketService {
    String getPath();
    void onConnect(WsSession session);
    void onMessage(WsSession session, String message);
    void onClose(WsSession session, int statusCode, String reason);
    void onError(WsSession session, Throwable throwable);
}
