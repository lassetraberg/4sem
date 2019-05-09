package commonVehicle.unitTest;

import commonAuthentication.domain.repository.IAccountRepository;
import commonvehicle.domain.model.vehicledata.Vehicle;
import commonvehicle.domain.repository.IVehicleRepository;
import commonvehicle.domain.service.IVehicleService;
import commonvehicle.domain.service.VehicleService;
import io.javalin.NotFoundResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.mockito.Matchers.anyString;

public class VehicleServiceTest {
    @Mock
    private IVehicleRepository vehicleRepository;
    @Mock
    private IAccountRepository accountRepository;

    private IVehicleService vehicleService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        vehicleService = new VehicleService(vehicleRepository, accountRepository);
    }

    @Test
    public void addData_NonExistentDevice_ShouldReturnFalse() {
        // Arrange
        Vehicle dummyVehicle = new Vehicle(UUID.randomUUID(), null, null, null, null, null);
        Mockito.when(vehicleRepository.getDevice(anyString())).thenReturn(null);

        // Act
        boolean result = vehicleService.addData(dummyVehicle);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void getData_UserDoesntOwnDevice_ShouldThrowException() {
        // Arrange
        Mockito.when(vehicleRepository.doesUserOwnDevice(anyString(), anyString())).thenReturn(false);
        NotFoundResponse ex = null;

        // Act
        try {
            vehicleService.getData(UUID.randomUUID(), null, null, null);
        } catch (NotFoundResponse nfr) {
            ex = nfr;
        }

        // Assert
        Assert.assertNotNull(ex);
    }

    @Test
    public void registerDevice_NoAccount_ShouldReturnFalse() {
        // Arrange
        Mockito.when(accountRepository.findByUsername(anyString())).thenReturn(null);

        // Act
        boolean result = vehicleService.registerDevice(null, null, null);

        // Assert
        Assert.assertFalse(result);
    }

}
