package simulation.hardware;

public class State {
    private String deviceId = "2905d0e7-615c-455b-8807-ddd7665d3994";
    private boolean shouldBrake = false;

    public boolean isShouldBrake() {
        return shouldBrake;
    }

    public void setShouldBrake(boolean shouldBrake) {
        this.shouldBrake = shouldBrake;
    }

    public String getDeviceId() {
        return deviceId;
    }
}
