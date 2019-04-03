package speedassistant.web;

import io.javalin.Context;
import speedassistant.domain.service.ISpeedAssistantService;

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
