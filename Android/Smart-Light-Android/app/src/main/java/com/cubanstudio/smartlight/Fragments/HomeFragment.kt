package com.cubanstudio.smartlight.Fragments

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.RenderMode
import com.airbnb.lottie.model.KeyPath
import com.airbnb.lottie.value.SimpleLottieValueCallback
import com.cubanstudio.smartlight.MainActivity
import com.cubanstudio.smartlight.R
import com.cubanstudio.smartlight.SocketClient.SocketClient

import com.google.android.material.button.MaterialButton
import com.google.android.material.slider.Slider
import org.jetbrains.anko.find


class HomeFragment : Fragment() {
    var effectsList =
        arrayListOf<String>("SOLID", "PULSING", "TWINKLE", "PHASING", "RAINBOW", "MUSIC")
    var choosenEffect = 0
    lateinit var bulbPic: LottieAnimationView

    var socket = SocketClient()
    var hue = 0;
    var saturation = 0;
    var value = 255;

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var view = inflater.inflate(R.layout.home_fragment, container, false)
        var ac = activity as MainActivity
        socket = ac.socketClient
        bulbPic = view.find<LottieAnimationView>(R.id.anim)

        // *  device MENU
        val addDevBut = view.find<MaterialButton>(R.id.adddevice)

        // * effect MENU
        val effectText = view.find<TextView>(R.id.effectText)
        val effButLeft = view.find<MaterialButton>(R.id.effectleft)
        val effButRight = view.find<MaterialButton>(R.id.effectright)

        // * color MENU
        val hueSlider = view.find<Slider>(R.id.HUEslider)
        val satSlider = view.find<Slider>(R.id.SATslider)
        val lightnessSlider = view.find<Slider>(R.id.LIGslider)
        val speedSlider = view.find<Slider>(R.id.SPEEDslider)
        bulbPic.setOnClickListener {
            socket.sendMessage("COLOR", "0", "0", "0")
        }
        bulbPic.setMaxFrame(45)


        bulbPic.setRenderMode(RenderMode.HARDWARE)
        lightBulbColor(
            bulbPic,
            Color.argb(1f, .98431372549f, 0.81176470588f, 0.0431372549f)
        )
        hueSlider.trackColor = ColorStateList.valueOf(
            Color.argb(1f, .98431372549f, 0.81176470588f, 0.0431372549f))
        hueSlider.thumbColor = ColorStateList.valueOf(
            Color.argb(1f, .98431372549f, 0.81176470588f, 0.0431372549f))
        satSlider.trackColor = ColorStateList.valueOf(
            Color.argb(1f, .98431372549f, 0.81176470588f, 0.0431372549f))
        satSlider.thumbColor = ColorStateList.valueOf(
            Color.argb(1f, .98431372549f, 0.81176470588f, 0.0431372549f))
        // * set Default color of light bulb
//("SOLID", "PULSING", "TWINKLE", "PHASING", "RAINBOW", "MUSIC")
        effButLeft.setOnClickListener {
            choosenEffect--
            if (choosenEffect < 0)
                choosenEffect = effectsList.size - 1
            effectText.text = effectsList[choosenEffect]
            socket.sendMessage("EFF", choosenEffect.toString())
            when(effectsList[choosenEffect]){
                "PHASING","RAINBOW","MUSIC"->{
                    hueSlider.visibility = View.INVISIBLE
                    satSlider.visibility = View.INVISIBLE
                }
                else ->{
                    hueSlider.visibility = View.VISIBLE
                    satSlider.visibility = View.VISIBLE
                }
            }
        }
        effButRight.setOnClickListener {
            choosenEffect++
            if (choosenEffect > effectsList.size - 1)
                choosenEffect = 0
            effectText.text = effectsList[choosenEffect]
            socket.sendMessage("EFF", choosenEffect.toString())
            when(effectsList[choosenEffect]){
                "PHASING","RAINBOW" ->{
                    hueSlider.visibility = View.INVISIBLE
                    satSlider.visibility = View.INVISIBLE
                }
                else ->{
                    hueSlider.visibility = View.VISIBLE
                    satSlider.visibility = View.VISIBLE
                }
            }
        }

        hueSlider.addOnChangeListener { slider, value, fromUser ->
            val colorsat = Color.HSVToColor(floatArrayOf(hueSlider.value*360, 1f-satSlider.value, 1f))
            val colorhue = Color.HSVToColor(floatArrayOf(hueSlider.value*360, 1f, 1f))
            lightBulbColor(bulbPic, colorsat)
            hueSlider.thumbColor = ColorStateList.valueOf(colorhue)
            hueSlider.trackColor = ColorStateList.valueOf(colorhue)
            satSlider.trackColor = ColorStateList.valueOf(colorsat)

            satSlider.thumbColor = ColorStateList.valueOf(colorsat)
            socket.sendMessage("HUE",((value*255).toInt()).toString())
        }
        satSlider.addOnChangeListener { slider, value, fromUser ->
            var ar : FloatArray = FloatArray(3)
            Color.colorToHSV(hueSlider.trackColor.defaultColor,ar)
            val color = Color.HSVToColor(floatArrayOf(ar[0], 1f-satSlider.value, 1f))

            lightBulbColor(bulbPic, color)
            satSlider.trackColor = ColorStateList.valueOf(color)
            satSlider.thumbColor = ColorStateList.valueOf(color)
            socket.sendMessage("SAT",(((1f-value)*255).toInt()).toString())

        }

        lightnessSlider.addOnChangeListener { slider, value, fromUser ->
            bulbPic.cancelAnimation()
            bulbPic.setMaxFrame(136);
            bulbPic.frame = value.toInt()
            val brightness:Int = (((value-90f)/46f)*255).toInt()
            socket.sendMessage("BRIGHT",brightness.toString())
        }

        speedSlider.addOnChangeListener { slider, value, fromUser ->
            socket.sendMessage("SPEED",value.toInt().toString())
        }


        satSlider.addOnSliderTouchListener(object: Slider.OnSliderTouchListener{
            override fun onStartTrackingTouch(slider: Slider) {
                slider.trackHeight=25
                slider.thumbRadius=35
            }

            override fun onStopTrackingTouch(slider: Slider) {
                slider.trackHeight=5
                slider.thumbRadius=0
            }

        })
        hueSlider.addOnSliderTouchListener(object: Slider.OnSliderTouchListener{
            override fun onStartTrackingTouch(slider: Slider) {
                slider.trackHeight=25
                slider.thumbRadius=35
            }


            override fun onStopTrackingTouch(slider: Slider) {
                slider.trackHeight=5
                slider.thumbRadius=0
            }
        })

        lightnessSlider.addOnSliderTouchListener(object: Slider.OnSliderTouchListener{
            override fun onStartTrackingTouch(slider: Slider) {
                slider.trackHeight=25
                slider.thumbRadius=35
            }

            override fun onStopTrackingTouch(slider: Slider) {
                slider.trackHeight=3
                slider.thumbRadius=0
            }
        })

        speedSlider.addOnSliderTouchListener(object :Slider.OnSliderTouchListener{
            override fun onStartTrackingTouch(slider: Slider) {
                slider.trackHeight=25
                slider.thumbRadius=35
            }

            override fun onStopTrackingTouch(slider: Slider) {
                slider.trackHeight=3
                slider.thumbRadius=0
            }
        })


        addDevBut.setOnClickListener {
            val ft = fragmentManager?.beginTransaction()
            ft?.replace(
                R.id.contain,
                DeviceListFragment()
            )
            ft?.addToBackStack("Presets")
            ft?.commit()
        }


        return view
    }


    private fun addDevice() {

    }


    private fun lightBulbColor(anim: LottieAnimationView, color: Int) {


        anim.addValueCallback(
            KeyPath("Bulb background", "**"),
            LottieProperty.COLOR_FILTER,
            SimpleLottieValueCallback<ColorFilter?> {
                PorterDuffColorFilter(
                    color,
                    PorterDuff.Mode.MULTIPLY
                )
            }
        )
        anim.addValueCallback(
            KeyPath("Light", "**"),
            LottieProperty.COLOR_FILTER,
            SimpleLottieValueCallback<ColorFilter?> {
                PorterDuffColorFilter(
                    color,
                    PorterDuff.Mode.MULTIPLY
                )
            }
        )
    }
}



