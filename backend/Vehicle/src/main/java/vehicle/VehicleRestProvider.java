package vehicle;

import common.spi.IRouterService;
import common.util.SPILocator;
import commonAuthentication.config.authConfig.Roles;
import commonAuthentication.domain.repository.IAccountRepository;
import commonvehicle.domain.repository.IVehicleRepository;
import commonvehicle.domain.service.IVehicleService;
import io.javalin.apibuilder.EndpointGroup;
import vehicle.web.VehicleController;

import static common.util.JavalinUtils.roles;
import static io.javalin.apibuilder.ApiBuilder.*;

public class VehicleRestProvider implements IRouterService {

    private VehicleController controller;

    public VehicleRestProvider() {
        IVehicleService vehicleService = SPILocator.locateSpecific(IVehicleService.class);
        IVehicleRepository vehicleRepository = SPILocator.locateSpecific(IVehicleRepository.class);
        IAccountRepository accountRepository = SPILocator.locateSpecific(IAccountRepository.class);

        vehicleService.setVehicleRepository(vehicleRepository);
        vehicleService.setAccountRepository(accountRepository);

        controller = new VehicleController(vehicleService);
    }

    @Override
    public EndpointGroup getRoutes() {
        return (() -> path("vehicle", () -> {
            post(controller::registerVehicle, roles(Roles.AUTHENTICATED));
            get(controller::getVehicles, roles(Roles.AUTHENTICATED));
            path(":device-id", () -> {
                get(controller::getVehicle, roles(Roles.AUTHENTICATED));
                delete(controller::deleteVehicle, roles(Roles.AUTHENTICATED));
                path("data", () -> {
                    get(controller::getVehicleData, roles(Roles.AUTHENTICATED));
                });
            });
        }));
    }
}