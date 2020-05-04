#include <Arduino.h>







void setup()
{
  // put your setup code here, to run once:
  Serial.begin(230400);
  
  
}
String s ="";
long t = 0;
void loop()
{
  t= micros();
  for (int i = 0; i < 10000; i++)
  {
    s+= "x";
  }
  Serial.println();
  Serial.println(micros()-t);
  while(1);
  // put your main code here, to run repeatedly:

  
}
