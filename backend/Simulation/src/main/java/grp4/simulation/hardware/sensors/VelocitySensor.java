package grp4.simulation.hardware.sensors;

import grp4.common.data.mqtt.topics.VariableMqttTopic;
import grp4.common.spi.IMqttService;
import grp4.simulation.hardware.AbstractDevice;
import grp4.simulation.hardware.State;

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
