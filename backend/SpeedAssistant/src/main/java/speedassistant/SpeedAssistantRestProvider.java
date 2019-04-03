package speedassistant;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.spi.IMqttService;
import common.spi.IRouterService;
import common.util.SPILocator;
import commonAuthentication.config.authConfig.Roles;
import io.javalin.apibuilder.EndpointGroup;
import speedassistant.domain.service.ISpeedLimitService;
import speedassistant.domain.service.SpeedLimitService;
import speedassistant.web.SpeedAssistantRestController;

import java.util.Collections;

import static io.javalin.apibuilder.ApiBuilder.*;

public class SpeedAssistantRestProvider implements IRouterService {


    private SpeedAssistantRestController controller;

    public SpeedAssistantRestProvider() {
        ObjectMapper mapper = new ObjectMapper();
        IMqttService mqttService = SPILocator.locateSpecific(IMqttService.class);

        ISpeedLimitService speedLimitService = new SpeedLimitService();

        controller = new SpeedAssistantRestController();
    }

    @Override
    public EndpointGroup getRoutes() {
        return () -> {
            path("/speed-assistant/vehicles/", () -> {
                get(controller::getVehicles, Collections.singleton(Roles.AUTHENTICATED));
                post(controller::addVehicle, Collections.singleton(Roles.AUTHENTICATED));
            });
        };
    }
}
