#include <Arduino.h>
#include <kiss_fft.h>



void setup()
{
  // put your setup code here, to run once:
  Serial.begin(230400);
}

void loop()
{
  kiss_fft_cfg cfg = kiss_fft_alloc( nfft ,is_inverse_fft ,0,0 );
  
}