package core.web;

import common.spi.IAccessManagerService;
import common.spi.IRouterService;
import common.spi.IWebSocketService;
import common.util.SPILocator;
import io.javalin.Javalin;

import java.util.List;

public class Router {

    public void register(Javalin app) {
        for (IRouterService router : locateRouterServices()) {
            app.routes(router.getRoutes());
        }

        for (IAccessManagerService configService : locateAccessManagerServices()) {
            app.accessManager(configService::configure);
        }

        for (IWebSocketService webSocketService : locateWebSocketServices()) {
            app.ws(webSocketService.getPath(), ws -> {
                ws.onConnect(webSocketService::onConnect);
                ws.onMessage(webSocketService::onMessage);
                ws.onClose(webSocketService::onClose);
                ws.onError(webSocketService::onError);
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
