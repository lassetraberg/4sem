package grp4.speedassistant.domain.service;

import grp4.commonvehicle.domain.model.vehicledata.GpsCoordinates;

public interface ISpeedLimitService {
    /**
     * Get the speed limit for coordinates.
     *
     * @param gpsCoordinates gps coordinates
     * @return returns the speed limit if one was found, otherwise Integer.MAX_VALUE
     */
    short getSpeedLimit(GpsCoordinates gpsCoordinates);
}
