
#include <FastLED.h>


void setupLight(int lednum);
void setEffectHue(uint8_t hue);
void setEffectSaturation(uint8_t saturation);
void setBrightness(uint8_t brightness);
void setEffect(int effect);
int getEffect();
void setSpeed(uint16_t s);
void updateLight();
void solid();
void pulse();
void twinkle();
void phasing();
void rainbow();
void music();
void forrest();
void sunrise();
void setDelay(uint8_t delay);
void setMusicPeak(double peak);
