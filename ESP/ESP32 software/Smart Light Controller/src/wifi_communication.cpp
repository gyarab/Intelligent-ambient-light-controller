#include <Packet.h>
#include <wifi_communication.h>
#include <memory_system.h>
#include <time_manager.h>
#include <WiFi.h>
#include <AsyncUDP.h>
#include <FastLED.h>
#include <SPI.h>
#include <Wire.h>

Packet pac;
AsyncUDP udp;
bool connected = false;

bool wifiSetup(char *ssid, char *pass)
{
    WiFi.mode(WIFI_STA);

    Packet connecting;
    connecting.setHead("CONNECTING");
    connecting.setBody("1");

    WiFi.begin(ssid, pass);
    Serial.println(ssid);
    Serial.println(pass);
    sendData(connecting);
    for (int i = 0; i < 15; i++)
    {
        if (WiFi.status() == WL_CONNECTED)
        {
            //! CONNECTED
            Serial.println("WIFI CONNECTED");

            connecting.setBody("0");
            sendData(connecting);
            //! SEND IP TO APP
            connecting.clearPacket();
            connecting.setHead("IPADDRESS");
            String ip = WiFi.localIP().toString();

            char ipC[ip.length() + 1];
            ip.toCharArray(ipC, ip.length() + 1);
            connecting.setBody(ipC);
            Serial.print("IP address ");
            Serial.println(ipC);
            Serial.println(connecting.head);
            Serial.println(connecting.body);
            sendData(connecting);
            connected = true;
            setupServer();
            return true;
            Packet p;
            p.setHead("SSID");
            p.setBody(ssid);
            writeToConfigFile(p);
            p.clearPacket();
            p.setHead("PASS");
            p.setBody(pass);
            writeToConfigFile(p);
            p.clearPacket();
        }
        delay(500);
    }
    Serial.println("WIFI CONNECT FAIL");
    connecting.setBody("2");
    sendData(connecting);
    return false;
}

bool wifiConnected(){
    return connected;
}

void setupServer()
{
    if (udp.listen(9003))
    {
        udp.onPacket([](AsyncUDPPacket packet) {
            Packet p;

            p.setData(packet.data(), packet.length());
            if (p.complete)
            {
                //! DO THINGS
                if (!strcmp(p.head, "EFF"))
                {
                    setEffect(atoi(p.body));
                }
                else if (!strcmp(p.head, "HUE"))
                {
                    setEffectHue(atoi(p.body));
                }
                else if (!strcmp(p.head, "SAT"))
                {
                    setEffectSaturation(atoi(p.body));
                }
                else if (!strcmp(p.head, "BRIGHT"))
                {
                    setBrightness(atoi(p.body));
                }
                else if (!strcmp(p.head, "SPEED"))
                {
                    setSpeed(atoi(p.body));
                }else if (!strcmp(p.head, "ALARM"))
                {
                    Serial.println(p.body);
                    
                    uint8_t id = 0;
                    uint8_t hour = 0;
                    uint8_t minute = 0;
                    uint8_t effect = 0;
                    uint8_t delay = 0;
                    uint8_t days = 0;

                    char inf[3]= {0};
                    
                   
                    for(int i =0;i<3;i++){
                        inf[i] = p.body[i];
                    }
                    id = atoi(inf);

                    for(int i =0;i<3;i++){
                        inf[i] = p.body[i+4];
                    }
                    hour = atoi(inf);
                    for(int i =0;i<3;i++){
                        inf[i] = p.body[i+8];
                    }
                    minute = atoi(inf);
                    for(int i =0;i<3;i++){
                        inf[i] = p.body[i+12];
                    }
                    effect = atoi(inf);
                    for(int i =0;i<3;i++){
                        inf[i] = p.body[i+16];
                    }
                    delay = atoi(inf);
                    for(int i =0;i<3;i++){
                        inf[i] = p.body[i+20];
                    }
                    days = atoi(inf);

                    newAlarm(id,hour,minute,effect,delay,days);
                }
                pac.clearPacket();
            }
        });
    }
}
