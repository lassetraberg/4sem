package grp4.core;

import grp4.core.config.ConfigFactory;

public class App {
    public static void main(String[] args) {
        ConfigFactory configFactory = new ConfigFactory();
        configFactory.getAppConfig().setup().start();
    }
}
