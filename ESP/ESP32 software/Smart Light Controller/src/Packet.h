#ifndef PACKET_H
#define PACKET_H
#include <Arduino.h>
class Packet
{
private:
    
    char checkSum[4];

public:
    size_t counter;
    char head[15];
    char body[40];
    
    boolean complete;

    Packet();
    void clearPacket();
    void calculateCheckSum(char* data,int length);
    char* buildPacket();
    boolean checkCheckSum();
    void appendData(char c);
    void setData(unsigned char * data,uint8_t length);
    void setHead(char * head);
    void setBody(char * body);
};
#endif