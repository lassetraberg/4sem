package grp4.core.web;

import grp4.common.spi.IAccessManagerService;
import grp4.common.spi.IRouterService;
import grp4.common.spi.IWebSocketService;
import grp4.common.util.SPILocator;
import io.javalin.Javalin;

import java.util.Collection;

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

    private Collection<IRouterService> locateRouterServices() {
        return SPILocator.locateAll(IRouterService.class);
    }

    private Collection<IAccessManagerService> locateAccessManagerServices() {
        return SPILocator.locateAll(IAccessManagerService.class);
    }

    private Collection<IWebSocketService> locateWebSocketServices() {
        return SPILocator.locateAll(IWebSocketService.class);
    }
}
