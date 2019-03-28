package core.config;

import common.config.Config;
import core.web.Router;

public class ConfigFactory {
    public AppConfig getAppConfig() {
        Router router = new Router();
        int port = Integer.parseInt(Config.getInstance().getProperty("port"));

        return new AppConfig(router, port);
    }
}
