package grp4.simulation.hardware.sensors;

import grp4.common.data.mqtt.topics.VariableMqttTopic;
import grp4.common.spi.IMqttService;
import grp4.simulation.hardware.AbstractDevice;
import grp4.simulation.hardware.State;

import java.util.concurrent.ThreadLocalRandom;

public class AccelerationSensor extends AbstractDevice {
    public AccelerationSensor(IMqttService client) {
        super(client);
    }

    @Override
    public void call(State state) {
        client.publish(VariableMqttTopic.VEHICLE_ACCELERATION, state.getDeviceId(), toJson(generateAcceleration()));
    }

    private String toJson(double acceleration) {
        return (String.format("{ \"acceleration\": %f }", acceleration));
    }

    private double generateAcceleration() {
        return ThreadLocalRandom.current().nextDouble(-4, 4);
    }
}
