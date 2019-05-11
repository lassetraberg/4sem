package grp4.common.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private Properties properties;
    private static Config ourInstance;

    private final static String ENVIRONMENT_PRODUCTION = "prod";
    private final static String ENVIRONMENT_DEVELOPMENT = "dev";

    public static Config getInstance() {
        if (ourInstance == null) {
            ourInstance = new Config();
        }
        return ourInstance;
    }

    private Config() {
        try {
            InputStream in = this.getClass().getClassLoader().getResourceAsStream("grp4/common/config.properties");
            properties = new Properties();
            properties.load(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String getProperty(String key) {
        String env = getEnv("env") == null ? ENVIRONMENT_DEVELOPMENT : getEnv("env");

        String property = properties.getProperty(env + "." + key);
        if (property == null) {
            property = properties.getProperty(key);
        }

        return property;
    }

    public String getEnv(String key) {
        return System.getenv(key);
    }

    public String getEnvOrDefaultProperty(String key) {
        String env = getEnv(key);
        if (env != null) {
            return env;
        } else {
            return getProperty(key);
        }
    }
}
