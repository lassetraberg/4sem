package vehicle;

import common.spi.IRouterService;
import common.util.SPILocator;
import commonAuthentication.config.authConfig.Role;
import commonAuthentication.domain.repository.IAccountRepository;
import commonvehicle.domain.repository.IVehicleRepository;
import commonvehicle.domain.service.IVehicleService;
import io.javalin.apibuilder.EndpointGroup;
import vehicle.domain.service.LicensePlateService;
import vehicle.web.VehicleController;

import static common.util.JavalinUtils.roles;
import static io.javalin.apibuilder.ApiBuilder.*;

public class VehicleRestProvider implements IRouterService {
    private VehicleController controller;
    private LicensePlateService licensePlateService;

    public VehicleRestProvider() {
        IVehicleService vehicleService = SPILocator.locateSpecific(IVehicleService.class);
        IVehicleRepository vehicleRepository = SPILocator.locateSpecific(IVehicleRepository.class);
        IAccountRepository accountRepository = SPILocator.locateSpecific(IAccountRepository.class);

        vehicleService.setVehicleRepository(vehicleRepository);
        vehicleService.setAccountRepository(accountRepository);

        licensePlateService = new LicensePlateService();
        controller = new VehicleController(licensePlateService, vehicleService);
    }

    @Override
    public EndpointGroup getRoutes() {
        return (() -> path("vehicle", () -> {
            post(controller::registerVehicle, roles(Role.AUTHENTICATED));
            get(controller::getVehicles, roles(Role.AUTHENTICATED));
            get("licenseplate/:license-plate", controller::getVehicleLicensePlateData, roles(Role.AUTHENTICATED));
            path(":device-id", () -> {
                get(controller::getVehicle, roles(Role.AUTHENTICATED));
                delete(controller::deleteVehicle, roles(Role.AUTHENTICATED));
                path("data", () -> {
                    get(controller::getVehicleData, roles(Role.AUTHENTICATED));
                    get(":from/:to", controller::getVehicleDataFromTo, roles(Role.AUTHENTICATED));
                });
            });
        }));
    }
}
