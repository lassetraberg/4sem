package grp4.vehicle.integrationTest;

import grp4.vehicle.domain.service.ILicensePlateService;
import grp4.vehicle.domain.service.LicensePlateService;
import io.javalin.NotFoundResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.Instant;

public class LicensePlateServiceTest {
    private ILicensePlateService licensePlateService;

    @Before
    public void setUp() {
        licensePlateService = new LicensePlateService();
    }

    @Test
    public void getData_NonExistentPlate_ShouldThrowException() {
        // Arrange
        String plate = "asdaasd";
        NotFoundResponse ex = null;

        // Act
        try {
            String data = licensePlateService.getData(plate);
        } catch (NotFoundResponse nfr) {
            ex = nfr;
        }

        // Assert
        Assert.assertNotNull(ex);
    }

    @Test
    public void getData_KnownPlate_ShouldReturnUsableData() {
        // Arrange
        String plate = "XK38231";

        // Act
        String data = licensePlateService.getData(plate);

        // Assert
        Assert.assertTrue(data.contains("SKODA"));
        Assert.assertTrue(data.contains("FABIA"));
        Assert.assertTrue(data.contains("Personbil"));
    }

    @Test
    public void getData_CachedData_ShouldBeFaster() {
        // Arrange
        String plate = "XK38231";

        // Act
        Instant firstStart = Instant.now();
        String firstResponse = licensePlateService.getData(plate);
        Instant firstEnd = Instant.now();
        int firstDuration = Duration.between(firstStart, firstEnd).getNano();

        Instant secondStart = Instant.now();
        String secondResponse = licensePlateService.getData(plate);
        Instant secondEnd = Instant.now();
        int secondDuration = Duration.between(secondStart, secondEnd).getNano();

        // Assert
        Assert.assertTrue(secondDuration < firstDuration);
    }
}
