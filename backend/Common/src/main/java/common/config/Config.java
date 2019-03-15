package common.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private Properties properties;
    private static Config ourInstance;

    public static Config getInstance(String module) {
        if (ourInstance == null) {
            ourInstance = new Config(module);
        }
        return ourInstance;
    }

    private Config(String module) {
        try {
            InputStream in = this.getClass().getClassLoader().getResourceAsStream(module + "/"+ module + "Config.properties");
            properties = new Properties();
            properties.load(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
