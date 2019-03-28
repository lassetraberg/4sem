// 4. Semester - OPB-II Project
// Group 4

/*
   Requires the following libraries:
   - WiFi101            by Arduino
   - pubsubclient       by knolleary
   - ArduinoJson        by Benoit Jackson

   All three libraries are available on Arduino Manager.
*/

/*
  ArduinoMqttClient - WiFi Simple Sender

  This example connects to a MQTT broker and publishes a message to
  a topic once a second.

  The circuit:
  - Arduino MKR 1000, MKR 1010 or Uno WiFi Rev.2 board

  This example code is in the public domain.
*/

const String id = "cc9d7c9b-fb0f-40d7-bd83-ba4d4e97e48b";

#include <ArduinoJson.h>
#include <PubSubClient.h>
#include <WiFi101.h> // for MKR1000 change to: #include <WiFi101.h>

#include "arduino_secrets.h"
///////please enter your sensitive data in the Secret tab/arduino_secrets.h
char ssid[] = SECRET_SSID;        // your network SSID (name)
char pass[] = SECRET_PASS;    // your network password (use for WPA, or use as key for WEP)

// To connect with SSL/TLS:
// 1) Change WiFiClient to WiFiSSLClient.
// 2) Change port value from 1883 to 8883.
// 3) Change broker value to a server with a known SSL/TLS root certificate
//    flashed in the WiFi module.

WiFiClient wifiClient;
PubSubClient mqttClient(wifiClient);

const char broker[] = "test.mosquitto.org";
int        port     = 1883;

const long interval = 2000;
unsigned long previousMillis = 0;

int count = 0;

void setupWifi() {
  Serial.print("Attemping to connect to SSID: ");
  Serial.println(ssid);

  // try to connect until successful
  while(WiFi.begin(ssid, pass) != WL_CONNECTED) {
    Serial.print(".");
    delay(5000);
  }

  Serial.println("You are connected to the network!");
  Serial.println("");
}

void setupMQTT() {
  Serial.print("Attempting to connect to the MQTT broker: ");
  Serial.println(broker);

  mqttClient.setServer(broker, port);
}

void reconnect() {
  while(!mqttClient.connected()) {
    if(mqttClient.connect("")) {
      Serial.print("Connection Established to: ");
      Serial.println(broker);
      Serial.println("");
    } else {
      Serial.print(".");
    }
  }
}

void sendMQTT(String topic, String json) {
  char charTopic[topic.length() + 1];
  topic.toCharArray(charTopic, topic.length() + 1);

  Serial.println(charTopic);
  
  mqttClient.beginPublish(charTopic, json.length(), true);
  mqttClient.print(json);
  mqttClient.endPublish();
}

void sendGPS() { 
  StaticJsonDocument<500> doc;

  doc["id"] = id;
  
  JsonObject gps = doc.createNestedObject("gps");
  gps["latitude"] = 55.396230;
  gps["longitude"] = 10.390600;

  String json;
  serializeJsonPretty(doc, json);
  
  sendMQTT(id + "/gps", json);
}

void sendVelocity() { 
  StaticJsonDocument<500> doc;

  doc["id"] = id;
  doc["velocity"] = 142;
 
  String json;
  serializeJsonPretty(doc, json);

  sendMQTT(id + "/velocity", json);
}

void sendMessage() { 
  StaticJsonDocument<500> doc;

  doc["id"] = id;
  doc["message"] = "OLE HAR EN KO INDE I SIT HUS!";
 
  String json;
  serializeJsonPretty(doc, json);

  sendMQTT(id + "/message", json);
}

void setup() {
  // Serial Monitor
  Serial.begin(9600);
  
  setupWifi();
  setupMQTT();
  reconnect();
}

void loop() {
  reconnect();
  mqttClient.loop();
 
  unsigned long currentMillis = millis();

  if (currentMillis - previousMillis >= interval) {
    // save the last time a message was sent
    previousMillis = currentMillis;

    sendGPS();
    sendVelocity();
    sendMessage();
  }
}
