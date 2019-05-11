package grp4.speedassistant.domain.dto;

public class SpeedLimitDto {
    private short speedLimit;

    public SpeedLimitDto(short speedLimit) {
        this.speedLimit = speedLimit;
    }

    public short getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(short speedLimit) {
        this.speedLimit = speedLimit;
    }
}
