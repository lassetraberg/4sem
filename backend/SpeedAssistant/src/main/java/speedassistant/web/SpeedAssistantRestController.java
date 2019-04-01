package speedassistant.web;

import common.util.JavalinUtils;
import io.javalin.Context;
import speedassistant.domain.VehicleData;
import speedassistant.domain.service.ISpeedAssistantService;

import java.util.UUID;

public class SpeedAssistantRestController {
    private ISpeedAssistantService speedAssistantService;

    public SpeedAssistantRestController(ISpeedAssistantService speedAssistantService) {
        this.speedAssistantService = speedAssistantService;
    }

    public void getVehicles(Context ctx) {
        ctx.status(200);
    }

    public void addVehicle(Context ctx) {
        ctx.status(200);
    }
}
