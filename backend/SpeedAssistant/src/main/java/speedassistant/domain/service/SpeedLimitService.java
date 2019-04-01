package speedassistant.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import speedassistant.domain.GpsCoordinates;

public class SpeedLimitService implements ISpeedLimitService{
    private ObjectMapper mapper;

    public SpeedLimitService(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public int getSpeedLimit(GpsCoordinates gpsCoordinates) {
        return 80;
    }
}
