#include <Light.h>

CRGB *leds;
CHSV *ledState;
CHSV currentColor;
uint16_t speed;
int mode = 0;
bool phase;
int LED_NUM;
long tim;
int freq = 0;
uint8_t t = 0;
bool delayActiv = false;
uint64_t timert = 0;
//13
//32
void setupLight(int lednum)
{
    LED_NUM = lednum;
    leds = new CRGB[lednum];
    ledState = new CHSV[lednum];
    FastLED.addLeds<WS2812, 32, GRB>(leds, lednum).setCorrection(TypicalLEDStrip);
    for (int i = 0; i < lednum; i++)
    {
        leds[i] = CRGB::Black;
        ledState[i] = CHSV(0, 0, 0);
    }
    speed = 0;
    mode = 0;
    currentColor = CHSV(0, 0, 0);
    FastLED.setBrightness(255);
    phase = true;
    FastLED.show();
    tim = millis();
}

void setEffect(int effect)
{
    mode = effect;
    //Serial.println(mode);
    if (mode == 2)
    {
        for (int i = 0; i < LED_NUM; i++)
        {
            ledState[i] = CHSV(0, 0, 0);
        }
    }
    if (effect == 3)
    {
        for (int i = 0; i < LED_NUM; i++)
        {
            ledState[i] = CHSV(currentColor.hue, 255, currentColor.value);
        }
    }
    else if (effect == 4)
    {

        for (int i = 0; i < LED_NUM; i++)
        {
            ledState[i] = CHSV(currentColor.hue + (i / 2), 255, currentColor.value);
        }
    }
}

void updateLight()
{
    if (millis() > tim + (speed))
    {
        if(delayActiv){
            timert += speed;
            if(timert/60000 >= t){
                setEffect(0);
                delayActiv = false;
            }
        }
        //Serial.println(".");
        switch (mode)
        {
        case 0:
            solid();
            break;
        case 1:
            pulse();
            break;
        case 2:
            twinkle();
            break;
        case 3:
            phasing();
            break;
        case 4:
            rainbow();
            break;
        case 5:
            music();
            break;
        case 6:
            sunrise();
            break;
        case 7:
            forrest();
            break;
        default:
            solid();
            break;
        }
        for (int l = 0; l < LED_NUM; l++)
        {
            hsv2rgb_rainbow(ledState[l], leds[l]);
        }
        tim = millis();
    }
    FastLED.show();
}

void setSpeed(uint16_t s)
{
    speed = s;
}

void setDelay(uint8_t delay){
    delayActiv = true;
    t = delay;
}

void setEffectHue(uint8_t hue)
{

    currentColor.hue = hue;
    // Serial.println(currentColor.hue);
}

void setEffectSaturation(uint8_t saturation)
{

    currentColor.saturation = saturation;
    // Serial.println(currentColor.saturation);
}

void setBrightness(uint8_t brightness)
{

    currentColor.value = brightness;
    // Serial.println(currentColor.value);
}

void solid()
{

    for (int i = 0; i < LED_NUM; i++)
    {
        ledState[i] = currentColor;
    }
}

void pulse()
{
    for (int i = 0; i < LED_NUM; i++)
    {
        ledState[i].hue = currentColor.hue;
        ledState[i].saturation = currentColor.saturation;
        if (phase)
        {
            ledState[i].value -= 1;
        }
        else
        {
            ledState[i].value += 1;
        }
    }
    if ((ledState[0].value < 40 && phase) || (ledState[0].value >= currentColor.value && !phase))
    {
        phase = !phase;
    }
}

void twinkle()
{
    for (int i = 0; i < LED_NUM; i++)
    {
        if (ledState[i].value > 0)
            ledState[i].value--;
    }
    if (random8() < 25)
    {
        ledState[random16(LED_NUM)] = currentColor;
    }
}

void phasing()
{
    for (int i = 0; i < LED_NUM; i++)
    {
        ledState[i].hue++;
        ledState[i].value = currentColor.value;
    }
}

void rainbow()
{
    for (int i = 0; i < LED_NUM; i++)
    {
        ledState[i].hue++;
        ledState[i].value = currentColor.value;
    }
}

void music()
{
    for (int i = 0; i < LED_NUM; i++)
    {
        if (ledState[i].value > 0)
            ledState[i].value -= 15;
    }
    for (int i = 0; i < freq / 20; i++)
    {
        CHSV c;
        if (i < 15)
        {
            c = CHSV(130, 255, 255);
        }
        else if (i >= 15 && i < 25)
        {
            c = CHSV(30, 255, 255);
        }
        else if (i >= 25)
        {
            c = CHSV(235, 255, 255);
        }
        ledState[99 - i] = c;
        ledState[100 + i] = c;
    }
}

void forrest()
{
    for (int i = 0; i < LED_NUM; i++)
    {
        int r = random8();
        CHSV c;
        if (r >= 0 && r < 64)
        {
            c = CHSV(90, 240, 230);
        }
        else if (r >= 64 && r < 128)
        {
            c = CHSV(64, 240, 230);
        }
        else if (r >= 128 && r < 192)
        {
            c = CHSV(32, 240, 230);
        }
        else if (r >= 192 && r < 256)
        {
            c = CHSV(110, 240, 230);
        }
        ledState[i] = c;
    }
}
void sunrise()
{
    for (int i = 0; i < LED_NUM; i++)
    {
        int r = random8();
        CHSV c;
        if (r >= 0 && r < 64)
        {
            c = CHSV(10, 240, 230);
        }
        else if (r >= 64 && r < 128)
        {
            c = CHSV(64, 240, 230);
        }
        else if (r >= 128 && r < 192)
        {
            c = CHSV(32, 240, 230);
        }
        else if (r >= 192 && r < 256)
        {
            c = CHSV(230, 240, 230);
        }
        ledState[i] = c;
    }
}
void setMusicPeak(double peak)
{
    Serial.print(peak);
    freq = 1000.0 * log10(peak) - 3900.0;
    if (freq < 0)
        freq = 0;
    Serial.print("  ");
    Serial.println(freq);
}

int getEffect()
{
    return mode;
}
