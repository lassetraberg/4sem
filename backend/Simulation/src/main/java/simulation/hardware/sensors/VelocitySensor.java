package simulation.hardware.sensors;

import common.data.mqtt.topics.VariableMqttTopic;
import common.spi.IMqttService;
import simulation.hardware.AbstractDevice;
import simulation.hardware.State;

import java.util.concurrent.ThreadLocalRandom;

public class VelocitySensor extends AbstractDevice {

    public VelocitySensor(IMqttService client) {
        super(client);
    }

    @Override
    public void call(State state) {
        client.publish(VariableMqttTopic.VEHICLE_VELOCITY, state.getDeviceId(), toJson(generateVelocity()));
    }

    private String toJson(int speed) {
        return String.format("{ \"velocity\": %d }", speed);
    }

    private int generateVelocity() {
        return ThreadLocalRandom.current().nextInt(0, 150);
    }
}
