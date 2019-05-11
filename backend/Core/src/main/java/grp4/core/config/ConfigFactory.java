package grp4.core.config;

import grp4.common.config.Config;
import grp4.core.web.Router;

public class ConfigFactory {
    public AppConfig getAppConfig() {
        Router router = new Router();
        int port = Integer.parseInt(Config.getInstance().getProperty("port"));

        return new AppConfig(router, port);
    }
}
