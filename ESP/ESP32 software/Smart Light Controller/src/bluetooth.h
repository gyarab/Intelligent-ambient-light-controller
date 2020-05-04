#include <Arduino.h>
#include <Packet.h>


void initBluetooth();
void checkBluetooth();
void executeCommand(Packet pac);
void connectToWiFi(Packet pac);
void sendData(Packet pac);
String fletcherCheckSum(String s);
void clearBuffer();