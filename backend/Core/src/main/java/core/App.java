package core;

import core.config.AppConfig;
import core.config.ConfigFactory;

public class App {
    public static void main(String[] args) {
        ConfigFactory configFactory = new ConfigFactory();
        configFactory.getAppConfig().setup().start();
    }
}
