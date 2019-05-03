// 4. Semester - OPB-II Project
// Group 4

/*
   Requires the following libraries:
   - WiFi101            by Arduino
   - pubsubclient       by knolleary
   - ArduinoJson        by Benoit Jackson
   - TinyGPS++          by mikalhart (Github)
   - OBD2UART           by stanleyhuangyc (Github)

  If not specified, the library can be found on the Arduino Library Manager.
*/

#include <ArduinoJson.h>
#include <TinyGPS++.h>
#include <SoftwareSerial.h>
#include <PubSubClient.h>
#include <WiFi101.h> // for MKR1000 change to: #include <WiFi101.h>
// #include <OBD2UART.h>

const String id = "cc9d7c9b-fb0f-40d7-bd83-ba4d4e97e48b";
const String mqttTopic = "/vehicle/" + id + "/";

const char broker[] = "test.mosquitto.org";
int        port     = 1883;

const long interval = 2000;
unsigned long previousMillis = 0;

static const int RXPin = 11, TXPin = 10;
static const uint32_t GPSBaud = 9600;

static const int redPin = A15;
static const int greenPin = A14;

static boolean speedAlarm = false;

// COBD obd;

// The TinyGPS++ object
TinyGPSPlus gps;

// The serial connection to the GPS device
SoftwareSerial ss(RXPin, TXPin);

WiFiClient wifiClient;
PubSubClient mqttClient(wifiClient);

void sendGPS() {
  if(gps.location.lat() == 0 && gps.location.lng() == 0) return;
  
  StaticJsonDocument<500> doc;
  
  doc["satellites"] = gps.satellites.value();

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
  
  sendMQTT(&mqttClient, "gps", json);
}

float acceleration = 0;
float velocity;

void sendVelocity() { 
  StaticJsonDocument<500> doc;

  velocity += acceleration;

  if(velocity < 0) velocity = 0;

  doc["velocity"] = velocity;
 
  String json;
  serializeJsonPretty(doc, json);

  sendMQTT(&mqttClient, "velocity", json);
}

void sendMessage(char* text) { 
  StaticJsonDocument<500> doc;

  doc["message"] = text;
 
  String json;
  serializeJsonPretty(doc, json);

  sendMQTT(&mqttClient, "message", json);
}

void sendAcceleration() { 
  StaticJsonDocument<500> doc;
  
  if(acceleration > 3) {
    acceleration += random(-1000, 0) / 1000.0;
  } else if(acceleration < -3) {
    acceleration += random(0, 1000) / 1000.0;
  } else {
    acceleration += random(-1000, 1000) / 1000.0;
  }

  doc["acceleration"] = acceleration;
 
  String json;
  serializeJsonPretty(doc, json);

  sendMQTT(&mqttClient, "acceleration", json);
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

static void toggleGreenLED(unsigned char brightness) {
  analogWrite(greenPin, brightness);
}

static void toggleRedLED(unsigned char brightness) {
  analogWrite(redPin, brightness);
}

boolean alarmBlink = false;

static void blinkLED() {
  if(speedAlarm) {
    alarmBlink = !alarmBlink;
    
    if(alarmBlink) {
      toggleRedLED(150);
      toggleGreenLED(150);
    } else {
      toggleRedLED(0);
      toggleGreenLED(0);
    }
  }
}

/*
 * 
 * SETUP AND LOOP
 * 
 */

void setup() {
  // Serial Monitor
  Serial.begin(9600);
  ss.begin(GPSBaud);
  
  setupWifi();
  setupMQTT(&mqttClient);
  reconnect(&mqttClient);
}

void loop() {  
  reconnect(&mqttClient);
  mqttClient.loop();
  
  unsigned long currentMillis = millis();

  if (currentMillis - previousMillis >= interval) {
    // save the last time a message was sent
    previousMillis = currentMillis;
    
    sendGPS();
    sendVelocity();
    sendMessage("Hallo World!");
    sendAcceleration();

    blinkLED();
    smartDelay(1000);
  }
}
