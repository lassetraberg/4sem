package grp4.simulation.hardware.actuators;

import grp4.common.data.mqtt.topics.VariableMqttTopic;
import grp4.common.spi.IMqttService;
import grp4.simulation.hardware.AbstractDevice;
import grp4.simulation.hardware.State;

public class SpeedingAlarmActuator extends AbstractDevice {
    public SpeedingAlarmActuator(IMqttService client) {
        super(client);
    }

    @Override
    public void call(State state) {
        client.subscribe(VariableMqttTopic.VEHICLE_ALARM_SPEEDING, state.getDeviceId(), (mqttTopic, msg) -> {
            if (msg.equalsIgnoreCase("1")) {
                state.setShouldBrake(true);
                System.out.println("You are speeding!");
            } else if (msg.equalsIgnoreCase("0")) {
                state.setShouldBrake(false);
                System.out.println("You are not speeding");
            }
        });
    }
}
