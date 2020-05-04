package com.cubanstudio.smartlight.Fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.cubanstudio.smartlight.Bluetooth.BluetoothService
import com.cubanstudio.smartlight.MainActivity

import com.cubanstudio.smartlight.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class WifiLoginFragment(private val wifiSSID:String,private val wifiBSSID: String,private var btService: BluetoothService) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var view = inflater.inflate(R.layout.wifi_login_fragment, container, false)
        var wifiName = view.findViewById<TextView>(R.id.loginName)
        wifiName.text = wifiSSID
        var connectBut = view.findViewById<MaterialButton>(R.id.connect)
        var passwordField = view.findViewById<TextInputEditText>(R.id.passwordInput)
        val incomeDataFilter = IntentFilter()
        incomeDataFilter.addAction("WILI") // WiFi network list
        incomeDataFilter.addAction("IPAD") // IP ADDRESS
        incomeDataFilter.addAction("CONNECTING")
        //incomeDataFilter.addAction("")
        //incomeDataFilter.addAction("")
        context?.registerReceiver(dataReceiver,incomeDataFilter)
        connectBut.setOnClickListener {
            btService.sendData("CONNECT",wifiSSID+"|"+passwordField.text.toString())
        }

        return view
    }


    val dataReceiver = object : BroadcastReceiver(){
        override fun onReceive(p0: Context?, intent: Intent?) {
            val incoming_data = intent?.getStringExtra("DATA")
            Log.e("INCOME",incoming_data)
            when(intent?.action){
                "WILI" -> {
                    /*if (!wifiArray.contains(incoming_data)){
                        wifiArray.add(incoming_data as String)
                        wifiArrayAdapter.notifyDataSetChanged()}*/
                }
                "IPAD" -> {

                    btService.disconnect()
                    var ac = activity as MainActivity
                    var socket = ac.socketClient
                    socket.addDevice(incoming_data!!,9003)

                    var edit =ac.getSharedPreferences(getString(R.string.IPadress),Context.MODE_PRIVATE).edit()
                    edit.putString("IP",incoming_data)
                    edit.apply()
                    fragmentManager?.popBackStack("Main",FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    val anim1 = TranslateAnimation(0f,200f,0f,0f)
                    anim1.duration = 500
                    val anim2 = AlphaAnimation(0f,1f)
                    anim2.duration = 350
                    val anim3 = AnimationSet(false)
                    anim3.addAnimation(anim1)
                    anim3.addAnimation(anim2)
                    val navBar =
                        activity?.findViewById<BottomNavigationView>(R.id.navbar)
                    navBar?.startAnimation(anim3)
                    navBar?.visibility = View.VISIBLE

                    //HomeFragment.address  = incoming_data as String

                }
                "CONNECTING" ->{
                    when(incoming_data){
                        "0" ->{
                            Log.e("CONNECTING","CONNECTED")


                        }
                        "1" ->{Log.e("CONNECTING",".............")}
                        "2" ->{Log.e("CONNECTING","FAILED")}
                    }

                }

            }
        }
    }

}
