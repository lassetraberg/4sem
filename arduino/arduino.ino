// 4. Semester - OPB-II Project
// Group 4

/*
   Requires the following libraries:
   - WiFi101            by Arduino
   - pubsubclient       by knolleary
   - ArduinoJson        by Benoit Jackson
   - TinyGPS++          by mikalhart (Github)

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
#include <TinyGPS++.h>
#include <SoftwareSerial.h>

#include "arduino_secrets.h"
///////please enter your sensitive data in the Secret tab/arduino_secrets.h
char ssid[] = SECRET_SSID;        // your network SSID (name)
char pass[] = SECRET_PASS;    // your network password (use for WPA, or use as key for WEP)

// To connect with SSL/TLS:
// 1) Change WiFiClient to WiFiSSLClient.
// 2) Change port value from 1883 to 8883.
// 3) Change broker value to a server with a known SSL/TLS root certificate
//    flashed in the WiFi module.

// Callback function header
void callback(char* topic, byte* payload, unsigned int length);

const char broker[] = "192.168.43.40";
int        port     = 1883;

const long interval = 2000;
unsigned long previousMillis = 0;

static const int RXPin = 11, TXPin = 10;
static const uint32_t GPSBaud = 9600;

// The TinyGPS++ object
TinyGPSPlus gps;

// The serial connection to the GPS device
SoftwareSerial ss(RXPin, TXPin);

WiFiClient wifiClient;
PubSubClient mqttClient(wifiClient);

void setupWifi() {
  Serial.print("Attemping to connect to SSID: ");
  Serial.println(ssid);

  // try to connect until successful
  while(WiFi.begin(ssid) != WL_CONNECTED) {
    Serial.println(".");
    delay(5000);
  }

  Serial.println("You are connected to the network!");
  Serial.println("");

  delay(5000);
}

void setupMQTT() {
  Serial.print("Attempting to connect to the MQTT broker: ");
  Serial.println(broker);

  mqttClient.setServer(broker, port);
  mqttClient.setCallback(callback);
}

void reconnect() {  
  while(!mqttClient.connected()) {
    String clientId = id;
    clientId += ":";
    clientId += String(random(0xffff), HEX);

    Serial.println(WiFi.status() == WL_CONNECTED);
    Serial.println(clientId);
   
    if(mqttClient.connect(clientId.c_str(), "sdugrp4", "password")) {
//       mqttClient.subscribe("/vehicle/" + id + "/alarms/speeding");
//      mqttClient.subscribe("/test");
      
      Serial.print("Connection Established to: ");
      Serial.println(broker);
      Serial.println("");
    } else {
      Serial.println(".");
      delay(5000);
    }
  }
}

void sendMQTT(String topic, String json) {
  topic = "/" + topic;
  
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
  doc["satelites"] = gps.satellites.value();

  // used for time and date
  char sz[32]; 
  
  sprintf(sz, "%02d/%02d/%02d", gps.date.month(), gps.date.day(), gps.date.year());
  doc["date"] = sz;
  sprintf(sz, "%02d:%02d:%02d", gps.time.hour(), gps.time.minute(), gps.time.second());
  doc["time"] = sz;
  
  JsonObject gpsJson = doc.createNestedObject("coordinates");
  gpsJson["lat"] = gps.location.lat();
  gpsJson["lon"] = gps.location.lng();

  String json;
  serializeJsonPretty(doc, json);
  Serial.println(gps.location.lat(), 8);
  Serial.println(gps.location.lng(), 8);
  
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
  byte* p = (byte*)malloc(length);
//   Copy the payload to the new buffer
  memcpy(p,payload,length);
  mqttClient.publish("/testo", p, length);

  String s = String((char*) p);

  Serial.println(topic);
  Serial.println(s);

  //   Free the memory
   free(p);
  
  //Serial.println("Callback on " + topic);
  //Serial.println(payload);
  //Serial.println();
}

// This custom version of delay() ensures that the gps object
// is being "fed".
static void smartDelay(unsigned long ms) {
  unsigned long start = millis();
  do {
    while (ss.available())
      gps.encode(ss.read());
  } while (millis() - start < ms);
}

void setup() {
  // Serial Monitor
//  Serial.begin(9600);
  Serial.begin(9600);
  ss.begin(GPSBaud);
  
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

    smartDelay(1000);
  }
}
