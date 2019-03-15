package core.config;

import core.web.Router;
import io.javalin.Javalin;

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
                .enableRouteOverview("/routes");
        router.register(app);

        return app;
    }
}
