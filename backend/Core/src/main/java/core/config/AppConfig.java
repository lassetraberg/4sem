package core.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mashape.unirest.http.Unirest;
import commonAuthentication.config.authConfig.Roles;
import core.web.ErrorExceptionMapper;
import core.web.Router;
import core.web.serializers.InstantSerializer;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;

import java.io.IOException;
import java.time.Instant;

import static common.util.JavalinUtils.roles;

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
                .enableRouteOverview("/routes", roles(Roles.ANYONE, Roles.AUTHENTICATED));
        router.register(app);
        ErrorExceptionMapper.register(app);
        initObjectMapper();
        enableCustomSerializers();

        return app;
    }

    private void initObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        Unirest.setObjectMapper(new com.mashape.unirest.http.ObjectMapper() {
            @Override
            public <T> T readValue(String value, Class<T> valueType) {
                try {
                    return mapper.readValue(value, valueType);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public String writeValue(Object value) {
                try {
                    return mapper.writeValueAsString(value);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void enableCustomSerializers() {
        ObjectMapper javalinMapper = JavalinJackson.getObjectMapper();
        JavaTimeModule timeModule = new JavaTimeModule();
        timeModule.addSerializer(Instant.class, new InstantSerializer());
        javalinMapper.registerModule(timeModule);
    }
}
