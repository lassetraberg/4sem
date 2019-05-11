package grp4.vehicle;

import grp4.common.spi.IRouterService;
import grp4.common.util.SPILocator;
import grp4.commonAuthentication.config.authConfig.Role;
import grp4.commonAuthentication.domain.repository.IAccountRepository;
import grp4.commonvehicle.domain.repository.IVehicleRepository;
import grp4.commonvehicle.domain.service.IVehicleService;
import grp4.vehicle.domain.service.LicensePlateService;
import grp4.vehicle.web.VehicleController;
import io.javalin.apibuilder.EndpointGroup;
import org.springframework.stereotype.Service;

import static grp4.common.util.JavalinUtils.roles;
import static io.javalin.apibuilder.ApiBuilder.*;

@Service
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
            get(controller::getVehicles, roles(Role.AUTHENTICATED, Role.ADMIN));
            get("licenseplate/:license-plate", controller::getVehicleLicensePlateData, roles(Role.AUTHENTICATED));
            get("all-data", controller::getAllVehiclesData, roles(Role.ADMIN));
            path(":device-id", () -> {
                get(controller::getVehicle, roles(Role.AUTHENTICATED, Role.ADMIN));
                delete(controller::deleteVehicle, roles(Role.AUTHENTICATED));
                path("data", () -> {
                    get(controller::getVehicleData, roles(Role.AUTHENTICATED, Role.ADMIN));
                    get(":from/:to", controller::getVehicleDataFromTo, roles(Role.AUTHENTICATED, Role.ADMIN));
                });
            });
        }));
    }
}
