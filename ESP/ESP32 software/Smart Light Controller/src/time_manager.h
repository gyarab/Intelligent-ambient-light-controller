#include <Arduino.h>
#include <NTPClient.h>
#include <WiFiUdp.h>
void time_managerConfig();
int isAlarmActivated();
void newAlarm(uint8_t id,uint8_t hour,uint8_t minute,uint8_t effect,uint8_t delay,uint8_t  days);
void deleteAlarm(uint32_t id);
