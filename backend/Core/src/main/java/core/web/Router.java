package core.web;

import common.spi.IAccessManagerService;
import common.spi.IRouterService;
import common.spi.IWebSocketHandler;
import common.spi.IWebSocketService;
import common.util.SPILocator;
import io.javalin.Javalin;

import java.util.List;

public class Router {

    public void register(Javalin app) {
        for (IAccessManagerService configService : locateAccessManagerServices()) {
            app.accessManager(configService::configure);
        }

        for (IRouterService router : locateRouterServices()) {
            app.routes(router.getRoutes());
        }

        for (IWebSocketService webSocketService : locateWebSocketServices()) {
            app.ws(webSocketService.getWebSocketHandler().getPath(), ws -> {
                ws.onConnect(webSocketService.getWebSocketHandler()::onConnect);
                ws.onMessage(webSocketService.getWebSocketHandler()::onMessage);
                ws.onClose(webSocketService.getWebSocketHandler()::onClose);
                ws.onError(webSocketService.getWebSocketHandler()::onError);
            });
        }
    }

    private List<IRouterService> locateRouterServices() {
        return SPILocator.locateAll(IRouterService.class);
    }

    private List<IAccessManagerService> locateAccessManagerServices() {
        return SPILocator.locateAll(IAccessManagerService.class);
    }

    private List<IWebSocketService> locateWebSocketServices() {
        return SPILocator.locateAll(IWebSocketService.class);
    }
}
