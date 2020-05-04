#include <Arduino.h>
#include <WiFi.h>

#include <time.h>
#include <bluetooth.h>
#include <memory_system.h>
#include <Packet.h>
#include <wifi_communication.h>
#include "arduinoFFT.h"
#include <time_manager.h>

#define SAMPLES 512
#define SAMPLING_FREQUENCY 20000
arduinoFFT FFT = arduinoFFT();
unsigned int sampling_period_us;
unsigned long microseconds;
double vReal[SAMPLES];
double vImag[SAMPLES];

TaskHandle_t lightUp;

void lightUpdates(void *pvParameters);
bool fourierTransform = false;

void setup()
{

  Serial.begin(230400);

  if (!initSPIFFS())
  {
    return;
  }

  if (isConfigAvailable())
  {
    Serial.println("Config is available");
    char *ssid = readConfig("SSID");
    char *pass = readConfig("PASS");
    char *LEDNUM = readConfig("LEDN");
    setupLight(atoi(LEDNUM)); //? promene lednum

    wifiSetup(ssid, pass);
    time_managerConfig();
  }
  initBluetooth();
  // Serial.println("1");

  xTaskCreatePinnedToCore(
      lightUpdates, "LIGHT", 10000, NULL, 1, &lightUp, 0);
  //pinMode(33, INPUT);

  //
  sampling_period_us = round(1000000 * (1.0 / SAMPLING_FREQUENCY));
  delay(500);
}

void loop()
{

  if (getEffect() == 5)
  {
    /*SAMPLING*/
    for (int i = 0; i < SAMPLES; i++)
    {
      microseconds = micros();
      vReal[i] = analogRead(33);
      vImag[i] = 0;
      while (micros() < (microseconds + sampling_period_us))
      {
      }
    }
    /*FFT*/
    FFT.DCRemoval(vReal, SAMPLES);
    FFT.Windowing(vReal, SAMPLES, FFT_WIN_TYP_HAMMING, FFT_FORWARD);
    FFT.Compute(vReal, vImag, SAMPLES, FFT_FORWARD);
    FFT.ComplexToMagnitude(vReal, vImag, SAMPLES);
    double f;
    double v;
    FFT.MajorPeak(vReal, SAMPLES, SAMPLING_FREQUENCY, &f, &v);
    setMusicPeak(v);
  }
  else
  {
    checkBluetooth();
  }
}

void lightUpdates(void *parameter)
{
  for (;;)
  {
    if (wifiConnected())
    {
      isAlarmActivated();
      updateLight();
    }
    delay(1);
  }
}
