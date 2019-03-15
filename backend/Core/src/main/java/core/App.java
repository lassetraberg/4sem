package core;

import core.config.ConfigFactory;
import org.eclipse.paho.client.mqttv3.MqttException;

public class App {
    public static void main(String[] args) throws MqttException {
        ConfigFactory configFactory = new ConfigFactory();
        configFactory.getAppConfig().setup().start();
    }
}
