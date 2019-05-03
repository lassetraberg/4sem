// Callback function header
void callback(char* topic, byte* payload, unsigned int length);

void setupMQTT(PubSubClient* mqttClient) {
  Serial.print("Attempting to connect to the MQTT broker: ");
  Serial.println(broker);

  mqttClient -> setServer(broker, port);
  mqttClient -> setCallback(callback);
}

void reconnect(PubSubClient* mqttClient) {  
  while(!mqttClient -> connected()) {
    String clientId = id;
    clientId += ":";
    clientId += String(random(0xffff), HEX);
    
    Serial.println(clientId);
   
//    if(mqttClient -> connect(clientId.c_str(), "sdugrp4", "password")) {
    if(mqttClient -> connect(clientId.c_str())) {
      Serial.println(mqttTopic + "alarms/#");
      mqttClient -> subscribe((mqttTopic + "alarms/#").c_str());
      
      Serial.print("Connection Established to: ");
      Serial.println(broker);
      Serial.println("");
    } else {
      Serial.println(".");
      delay(5000);
    }
  }
}

void sendMQTT(PubSubClient* mqttClient, String topic, String json) {
  topic = mqttTopic + topic;
  
  char charTopic[topic.length() + 1];
  topic.toCharArray(charTopic, topic.length() + 1);

  Serial.println(charTopic);
  
  mqttClient -> beginPublish(charTopic, json.length(), true);
  mqttClient -> print(json);
  mqttClient -> endPublish();
}

// Callback function
void callback(char* topic, byte* payload, unsigned int length) {
  // In order to republish this payload, a copy must be made
  // as the orignal payload buffer will be overwritten whilst
  // constructing the PUBLISH packet.

  // Allocate the correct amount of memory for the payload copy
  //byte* p = (byte*)malloc(length);
  // Copy the payload to the new buffer
  //memcpy(p,payload,length);
  //client.publish("outTopic", p, length);
  // Free the memory
  // free(p);

//   Allocate the correct amount of memory for the payload copy
//  byte* p = (byte*)malloc(length);
//   Copy the payload to the new buffer
//  memcpy(p,payload,length);
//  mqttClient.publish("/testo", p, length);

//  String s = String((char*) p);

//  Serial.println(topic);
//  Serial.println(s);

  //   Free the memory
//   free(p);

  //Serial.println("Callback on " + topic);
  //Serial.println(payload);
  //Serial.println();


  // Allocate the correct amount of memory for the payload copy
  byte* p = (byte*) malloc(length);
  
  // Copy the payload to the new buffer
  memcpy(p, payload, length);

  String s = String((char*) p);

  if(String(topic).equals(mqttTopic + "alarms/speeding")) {
    speedAlarm = false;
    
    if(s.equals("0")) {
      toggleGreenLED(255);
      toggleRedLED(0);
    } else if(s.equals("1")) {
      toggleGreenLED(0);
      toggleRedLED(255);
    } else {
      speedAlarm = true;
    }
  }

  free(p);
}
