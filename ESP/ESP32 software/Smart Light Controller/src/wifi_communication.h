#include <Arduino.h>
#include <bluetooth.h>
#include <Light.h>

bool wifiSetup(char *ssid, char *pass);
void setupServer();
bool wifiConnected();
