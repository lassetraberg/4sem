package simulation.hardware.actuators;

import common.data.mqtt.MqttConnection;
import common.data.mqtt.topics.VariableMqttTopic;
import simulation.hardware.AbstractDevice;
import simulation.hardware.State;

public class SpeedingAlarmActuator extends AbstractDevice {
    public SpeedingAlarmActuator(MqttConnection client) {
        super(client);
    }

    @Override
    public void call(State state) {
        client.subscribe(VariableMqttTopic.VEHICLE_ALARM_SPEEDING, state.getDeviceId(), (mqttTopic, msg) -> {
            if (msg.equalsIgnoreCase("true")) {
                state.setShouldBrake(true);
                System.out.println("You are speeding!");
            } else if (msg.equalsIgnoreCase("false")) {
                state.setShouldBrake(false);
                System.out.println("You are not speeding");
            }
        });
    }
}
