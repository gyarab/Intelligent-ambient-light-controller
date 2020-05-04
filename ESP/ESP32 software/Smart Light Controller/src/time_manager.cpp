#include <time_manager.h>
#include "time.h"
#include <Alarm.h>
#include <WiFi.h>
#include <Light.h>

Alarm alarms[50];
uint8_t numAlarms = 0;

WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP);

time_t cas;
void time_managerConfig()
{
    timeClient.begin();
    timeClient.setTimeOffset(7200);
   
}

int isAlarmActivated()
{
    if (!timeClient.update())
    {
        timeClient.forceUpdate();
    }
    uint8_t dayOfWeek = timeClient.getDay() - 1;
    if (dayOfWeek > 6)
    {
        dayOfWeek = 6;
    }
    for (int i=0;i<numAlarms;i++)
    {
        if((alarms[i].days>> (6-dayOfWeek))&1){
            if (alarms[i].hour == timeClient.getHours())
            {
                if (alarms[i].minute == timeClient.getMinutes())
                {
                    setEffect(alarms[i].effect);
                    setDelay(alarms[i].delay);
                    break;
                }
                
            }
        }
    }
    return 0;
}

void newAlarm(uint8_t id, uint8_t hour, uint8_t minute, uint8_t effect, uint8_t delay, uint8_t days)
{
    Serial.println(id);
    Serial.println(hour);
    Serial.println(minute);
    Serial.println(effect);
    Serial.println(delay);
    Serial.println(days);
    
    if (numAlarms < 50)
    {
        alarms[numAlarms++].newAlarm(id, hour, minute, effect, delay, days);
    }
}

void deleteAlarm(uint32_t id)
{
    if (numAlarms > 0)
    {
        bool deleted = false;
        for (int i = 0; i < numAlarms; i++)
        {
            if (deleted && numAlarms > 1)
            {
                alarms[i - 1] = alarms[i];
            }
            if (alarms[i].id == id)
            {
                alarms[i].~Alarm();
                deleted = true;
                ;
            }
        }
        numAlarms--;
    }
}
