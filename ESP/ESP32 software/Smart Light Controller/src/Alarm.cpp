#include <Alarm.h>

class Alarm;
void Alarm::newAlarm(uint32_t id,uint8_t hour,uint8_t minute,uint8_t effect,uint8_t delay,uint8_t days){
    this->id = id;
    this->hour = hour;
    this->minute = minute;
    this->effect = effect;
    this->delay = delay;
    this->days= days;
    

}
Alarm::Alarm(){
    id=0;
    hour=0;
    minute=0;
    effect=0;
    delay=0;
    days=0x00000000;
}
Alarm::~Alarm(){
    delete this;
}
    
