#ifndef ALARM_H
#define ALARM_H
#include <Arduino.h>

class Alarm
{
    
 public:
    uint8_t hour;
    uint8_t minute;
    uint32_t id;
    uint8_t effect;
    uint8_t delay;
    uint8_t days;
    Alarm();
    void newAlarm(uint32_t id,uint8_t hour,uint8_t minute,uint8_t effect,uint8_t delay,uint8_t days);
    
    ~Alarm();

};
#endif