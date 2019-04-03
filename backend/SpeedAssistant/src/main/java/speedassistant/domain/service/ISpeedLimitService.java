package speedassistant.domain.service;

import speedassistant.domain.models.vehicledata.GpsCoordinates;

public interface ISpeedLimitService {
    /**
     * Get the speed limit for coordinates.
     *
     * @param gpsCoordinates gps coordinates
     * @return returns the speed limit if one was found, otherwise Integer.MAX_VALUE
     */
    int getSpeedLimit(GpsCoordinates gpsCoordinates);
}
