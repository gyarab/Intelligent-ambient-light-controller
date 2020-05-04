#include <Packet.h>

class Packet;
Packet::Packet()
{
    clearPacket();
}

void Packet::calculateCheckSum(char *data, int length)
{
    long c1 = 0;
    long c2 = 0;
    for (int i = 0; i < length - 5; i++)
    {
        c1 += data[i];
        c2 += c1;
    }
    c1 %= 255;
    c2 %= 255;
    if (c1 / 16 < 10)
        data[length - 5] = (c1 / 16) + 48;
    else
        data[length - 5] = (c1 / 16) + 55;
    if (c1 % 16 < 10)
        data[length - 4] = (c1 % 16) + 48;
    else
        data[length - 4] = (c1 % 16) + 55;
    if (c2 / 16 < 10)
        data[length - 3] = (c2 / 16) + 48;
    else
        data[length - 3] = (c2 / 16) + 55;
    if (c1 % 16 < 10)
        data[length - 2] = (c2 % 16) + 48;
    else
        data[length - 2] = (c2 % 16) + 55;
}

char *Packet::buildPacket()
{
    int len = strlen(head) + strlen(body) + 8;
    char out[len];
    out[0] = '\1';
    for (int i = 0; i < strlen(head); i++)
    {
        out[i + 1] = head[i];
    }
    out[strlen(head) + 1] = '\2';
    for (int i = 0; i < strlen(body); i++)
    {
        out[i + 2 + strlen(head)] = body[i];
    }
    out[strlen(head) + strlen(body) + 2] = '\3';
    calculateCheckSum(out, len);
    out[len - 1] = '\4';
    counter = strlen(out);
    return out;
}

void Packet::clearPacket()
{
    for (int i = 0; i < 15; i++)
    {
        head[i] = '\0';
    }
    for (int i = 0; i < 40; i++)
    {
        body[i] = '\0';
    }
    for (int i = '\0'; i < 4; i++)
    {
        checkSum[i] = '\0';
    }

    counter = 0;
    complete = false;
}

void Packet::setHead(char *head)
{
    for (int i = 0; i < strlen(head); i++)
    {
        this->head[i] = head[i];
    }
}
void Packet::setBody(char *body)
{
    for (int i = 0; i < strlen(body); i++)
    {
        this->body[i] = body[i];
    }
}
void Packet::appendData(char c)
{

    // switch (c)
    // {

    // case (byte)0x01:
    //     clearPacket();
    //     counter = 1;
    //     break;

    // case (byte)0x02:
    //     counter = 2;
    //     // Serial.println(head);
    //     break;

    // case (byte)0x03:
    //     if (head == "SSID" || head == "PASS" || head == "LEDN")
    //     {
    //         complete = true;
    //     }

    //     // Serial.println(body);
    //     counter = 3;
    //     break;

    // case (byte)0x04:
    //     complete = true;
    //     break;

    // default:
    //     switch (counter)
    //     {

    //     case 1:
    //         head += c;
    //         break;

    //     case 2:
    //         body += c;
    //         break;

    //     case 3:
    //         checkSum += c;
    //         break;
    //     }
    //     break;
    // }
}

void Packet::setData(unsigned char *data, uint8_t length)
{
    int start = 0;
    int end = 0;

    for (int i = 0; i < length; i++)
    {
        switch (data[i])
        {
        case 1:
            start = i;
            break;
        case 2:
            end = i;
            for (int j = 0; j < end - start - 1; j++)
            {
                head[j] = data[start + j + 1];
            }
            start = i;
            break;
        case 3:

            end = i;
            for (int j = 0; j < end - start - 1; j++)
            {
                body[j] = data[start + j + 1];
            }
            break;
        case 4:
            complete = true;
            break;
        }
    }
}