package core.web;

import common.spi.IRouterService;
import common.util.SPILocator;
import io.javalin.Javalin;

import java.util.List;

public class Router {

    public void register(Javalin app) {
        for (IRouterService router : locateRouterServices()) {
            app.routes(router.getRoutes());
        }
    }

    private List<IRouterService> locateRouterServices() {
        return SPILocator.locateAll(IRouterService.class);
    }
}
