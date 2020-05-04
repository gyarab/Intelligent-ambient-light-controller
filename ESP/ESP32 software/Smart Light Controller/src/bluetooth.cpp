#include <BluetoothSerial.h>
#include <bluetooth.h>
#include <wifi_communication.h>

BluetoothSerial BLE;
const char verification[] = "$=EF%@gR;[M+SLWx*i%m@qA}x6kDl.";
Packet p;
uint8_t buffer[60];
uint8_t counter= 0;
bool packetAvailable = false;

void initBluetooth()
{
    BLE.begin("Smart Light");
    
}

void checkBluetooth()
{
    uint8_t in;

    if (BLE.available())
    { 
        in = BLE.read();
        Serial.write(in);
        if(in=='\1' && !packetAvailable){\
            clearBuffer();
            
            buffer[counter] = in;
            counter++;
            packetAvailable = true;            
        }else
        if(packetAvailable){
            buffer[counter] = in;
            counter ++;
            if(in == '\4'){
                p.setData((unsigned char *)buffer,strlen((char *)buffer));
                executeCommand(p);
                p.clearPacket();
                packetAvailable = false;
            }
        }
    }
}

void clearBuffer(){
    for(int i = 0; i < 60;i++){
        buffer[i] = '\0';
    }
    counter = 0;
}

void executeCommand(Packet pac)
{
    if (!strcmp(pac.head,"VER"))
    {
        Serial.print(strcmp(pac.body,verification));
    }
     else if (!strcmp(pac.head,"CONNECT"))
    {
        connectToWiFi(pac);
    }
}


void connectToWiFi(Packet pac){
    uint8_t dividerIndex = 255;
        for(int i = 0; i<strlen(pac.body);i++)
        {
            if(pac.body[i]=='|'){
                dividerIndex = i;
                break;
            }
        }
        char ssid[dividerIndex];
        char pass[strlen(pac.body)-dividerIndex];

        for(int i = 0;i<dividerIndex;i++){
            ssid[i] = pac.body[i];
        }
        for(int i = 0;i<strlen(pac.body)-dividerIndex;i++){
            pass[i] = pac.body[dividerIndex+i+1];
        }
        //! WIFI CONNECT ssid pass
        //wifiSetup(ssid,pass);
        
        
}

//! not done yet

void sendData(Packet pac)
{   
     char *c =pac.buildPacket();

    BLE.write((uint8_t *)c,strlen(c));
}

