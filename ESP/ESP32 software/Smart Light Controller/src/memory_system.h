#include <bluetooth.h>
#include <SPIFFS.h>
#include <Packet.h>
bool initSPIFFS();
bool isConfigAvailable();
bool writeToConfigFile(Packet pac);
char* readConfig(char* flag);
