package speedassistant.domain.service;

import speedassistant.domain.GpsCoordinates;

public interface ISpeedLimitService {
    int getSpeedLimit(GpsCoordinates gpsCoordinates);
}
