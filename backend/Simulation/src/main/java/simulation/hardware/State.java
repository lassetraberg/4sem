package simulation.hardware;

import java.util.UUID;

public class State {
    //    private UUID deviceId = UUID.fromString("2905d0e7-615c-455b-8807-ddd7665d3994");
    private UUID deviceId = UUID.fromString("cc9d7c9b-fb0f-40d7-bd83-ba4d4e97e48b");

    private boolean shouldBrake = false;

    public boolean isShouldBrake() {
        return shouldBrake;
    }

    public void setShouldBrake(boolean shouldBrake) {
        this.shouldBrake = shouldBrake;
    }

    public UUID getDeviceId() {
        return deviceId;
    }
}
