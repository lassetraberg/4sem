package core;

import core.config.ConfigFactory;
import org.eclipse.paho.client.mqttv3.MqttException;

public class App {
    public static void main(String[] args) {
        ConfigFactory configFactory = new ConfigFactory();
        configFactory.getAppConfig().setup().start();
    }
}
