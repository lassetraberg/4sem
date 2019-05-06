#include "arduino_secrets.h"

///////please enter your sensitive data in the Secret tab/arduino_secrets.h
char ssid[] = SECRET_SSID;        // your network SSID (name)
char pass[] = SECRET_PASS;    // your network password (use for WPA, or use as key for WEP)

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
