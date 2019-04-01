package speedassistant.web;

import common.data.mqtt.topics.StaticMqttTopic;
import common.spi.IMqttService;
import common.util.StringUtils;
import common.web.AbstractWebSocketController;
import io.javalin.websocket.WsSession;
import speedassistant.domain.service.ISpeedAssistantCommunication;

import java.util.UUID;

public class SpeedAssistantWSController extends AbstractWebSocketController implements ISpeedAssistantCommunication {

    private IMqttService mqttService;
    private ISpeedAssistantCommunication mqttCommunicationService;

    public SpeedAssistantWSController(IMqttService mqttService, ISpeedAssistantCommunication mqttCommunicationService) {
        this.mqttService = mqttService;
        this.mqttCommunicationService = mqttCommunicationService;

        establishSubscriptions();
    }

    public void onGpsMessage(UUID deviceId, String msg) {
        sendTo(deviceId, msg);
    }

    public void onVelocityMessage(UUID deviceId, String msg) {
        sendTo(deviceId, msg);
    }

    private void establishSubscriptions() {
        mqttService.subscribe(StaticMqttTopic.ALL_VEHICLES_GPS, (topic, s) -> {
            UUID deviceId = StringUtils.getUUIDFromTopic(topic);
            WsSession session = this.sessionMap.get(deviceId);

            if (session != null && session.pathParam("data").equalsIgnoreCase("gps")) {
                this.onGpsMessage(deviceId, s);
            }

            mqttCommunicationService.onGpsMessage(deviceId, s);
        });

        mqttService.subscribe(StaticMqttTopic.ALL_VEHICLES_VELOCITY, (topic, s) -> {
            UUID deviceId = StringUtils.getUUIDFromTopic(topic);
            WsSession session = this.sessionMap.get(deviceId);

            if (session != null && session.pathParam("data").equalsIgnoreCase("velocity")) {
                this.onVelocityMessage(deviceId, s);
            }

            mqttCommunicationService.onVelocityMessage(deviceId, s);
        });
    }


}
