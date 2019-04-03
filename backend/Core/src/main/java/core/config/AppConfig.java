package core.config;

import commonAuthentication.config.authConfig.Roles;
import core.web.ErrorExceptionMapper;
import core.web.Router;
import io.javalin.Javalin;

import java.util.Collections;

public class AppConfig {

    private Router router;
    private int port;

    public AppConfig(Router router, int port) {
        this.router = router;
        this.port = port;
    }

    public Javalin setup() {
        Javalin app = Javalin.create()
                .enableCorsForAllOrigins()
                .port(port)
                .enableRouteOverview("/routes", Collections.singleton(Roles.ANYONE));
        router.register(app);
        //ErrorExceptionMapper.register(app);

        return app;
    }
}
