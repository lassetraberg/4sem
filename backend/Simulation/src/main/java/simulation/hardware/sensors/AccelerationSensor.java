package simulation.hardware.sensors;

import common.data.mqtt.topics.VariableMqttTopic;
import common.spi.IMqttService;
import simulation.hardware.AbstractDevice;
import simulation.hardware.State;

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
