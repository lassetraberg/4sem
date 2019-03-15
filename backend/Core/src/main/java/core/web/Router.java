package core.web;

import common.spi.IConfigurationService;
import common.spi.IRouterService;
import common.util.SPILocator;
import io.javalin.Javalin;

import java.util.List;

public class Router {

    public void register(Javalin app) {
        for (IRouterService router : locateRouterServices()) {
            app.routes(router.getRoutes());
        }

        for (IConfigurationService configService : locateConfigServices()) {
            configService.configure(app);
        }
    }

    private List<IRouterService> locateRouterServices() {
        return SPILocator.locateAll(IRouterService.class);
    }

    private List<IConfigurationService> locateConfigServices() {
        return SPILocator.locateAll(IConfigurationService.class);
    }
}
