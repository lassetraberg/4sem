package vehicle.web;

import common.domain.model.Response;
import common.util.JavalinUtils;
import commonvehicle.domain.model.vehicledata.Vehicle;
import commonvehicle.domain.service.IVehicleService;
import io.javalin.Context;
import org.eclipse.jetty.http.HttpStatus;
import vehicle.domain.dto.VehicleRegistrationDto;

import java.util.UUID;

public class VehicleController {

    private IVehicleService vehicleService;

    public VehicleController(IVehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    public void registerVehicle(Context ctx) {
        VehicleRegistrationDto vehicleRegistrationDto = ctx.validatedBodyAsClass(VehicleRegistrationDto.class)
                .check(it -> it.getDeviceId() != null && !it.getDeviceId().isEmpty())
                .check(it -> it.getLicensePlate() != null && !it.getLicensePlate().isEmpty()).getOrThrow();

        String username = JavalinUtils.getUsername(ctx);

        boolean success = vehicleService.registerDevice(UUID.fromString(vehicleRegistrationDto.getDeviceId()), username, vehicleRegistrationDto.getLicensePlate());

        if (success) {
            ctx.json(new Response(HttpStatus.CREATED_201, "Success")).status(HttpStatus.CREATED_201);
        } else {
            ctx.json(new Response(HttpStatus.BAD_REQUEST_400, "Vehicle registration was unsuccessful")).status(HttpStatus.BAD_REQUEST_400);
        }
    }

    public void getVehicles(Context ctx) {
        String username = JavalinUtils.getUsername(ctx);

        ctx.json(vehicleService.getDevices(username));
    }

    public void getVehicle(Context ctx) {
        String username = JavalinUtils.getUsername(ctx);
        String deviceId = ctx.pathParam("device-id");

        ctx.json(vehicleService.getDevice(username, deviceId));
    }

    public void deleteVehicle(Context ctx) {
        String username = JavalinUtils.getUsername(ctx);
        String deviceId = ctx.pathParam("device-id");

        boolean success = vehicleService.deleteDevice(UUID.fromString(deviceId), username);

        Response res;
        if (success) {
            res = new Response(HttpStatus.OK_200, "Success");
        } else {
            res = new Response(HttpStatus.BAD_REQUEST_400, "Vehicle deletion failed");
        }

        ctx.json(res).status(res.getStatus());
    }

    public void getVehicleData(Context ctx) {
        String username = JavalinUtils.getUsername(ctx);
        String deviceId = ctx.pathParam("device-id");

        Vehicle vehicleData = vehicleService.getData(UUID.fromString(deviceId), username);

        ctx.json(vehicleData);
    }
}
