package common.spi;

import io.javalin.Javalin;

public interface IConfigurationService {
    void configure(Javalin app);
}
