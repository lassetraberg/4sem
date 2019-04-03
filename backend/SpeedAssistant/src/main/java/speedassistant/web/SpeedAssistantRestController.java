package speedassistant.web;

import io.javalin.Context;

public class SpeedAssistantRestController {

    public SpeedAssistantRestController() {
    }

    public void getVehicles(Context ctx) {
        ctx.status(200);
    }

    public void addVehicle(Context ctx) {
        ctx.status(200);
    }
}
