#include <memory_system.h>
#include <wifi_communication.h>
bool initSPIFFS()
{
    if (!SPIFFS.begin(true))
    {
        Serial.println("An error has ocured during startup");
        return false;
    }
    return true;
}
bool isConfigAvailable()
{   
    
    return SPIFFS.exists("/config.txt");
}

bool writeToConfigFile(Packet pac)
{

    File file = SPIFFS.open("/config.txt", FILE_READ);
    char conf[file.size()];

    for (int i = 0; i < file.size(); i++)
    {
        conf[i] = file.read();
    }
    file.close();

    file = SPIFFS.open("/config.txt", FILE_WRITE);
    if (!file)
    {
        //! FAILED WRITING TO FILE
        Serial.println("Failed to open file for writing");
        return false;
    }
    file.println(pac.buildPacket());
    for (char c : conf)
    {
        file.write(c);
    }

    file.close();

    return true;
}

char* readConfig(char *flag)
{
    File file = SPIFFS.open("/config.txt", FILE_READ);
    char buffer[60];
    int counter = 0;
    bool datAvailable = false;
    char in;
    Packet conf;
    while (file.available())
    {

        in = file.read();
        if (in == '\1' && !datAvailable)
        {
            for (int i = 0; i < 60; i++)
            {
                buffer[i] = '\0';
            }
            counter = 0;
            buffer[counter] = in;
            counter++;
            datAvailable = true;
        }
        else if (datAvailable)
        {
            buffer[counter] = in;
            counter++;
            if (in == '\4')
            {
                conf.setData((unsigned char *)buffer, strlen(buffer));
                datAvailable = false;
                if (!strcmp(conf.head, flag))
                {
                    // Serial.print(conf.head);
                    // Serial.print("  ");
                    // Serial.println(conf.body);
                    return conf.body;
                    
                }
                
            }
        }
    }
    return conf.body;
}
       