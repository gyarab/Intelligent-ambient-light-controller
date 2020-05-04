package com.cubanstudio.smartlight.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.cubanstudio.smartlight.Adapters.DeviceAdapter
import com.cubanstudio.smartlight.Bluetooth.BluetoothService

import com.cubanstudio.smartlight.R

class DeviceListFragment: Fragment() {
    lateinit var btService: BluetoothService
    private val key = "\$=EF%@gR;[M+SLWx*i%m@qA}x6kDl."
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.device_list_fragment, container, false)
        var text = ArrayList<String>()
        text.add("Addressable LED Strip")
        var img = listOf<Int>(R.drawable.led_strip)
        val adapter = DeviceAdapter(
            context!!.applicationContext,
            text
        )
        val listView=  view.findViewById<ListView>(R.id.devices)
        listView.adapter = adapter
        val appcontext = context!!.applicationContext
        btService = BluetoothService(context!!.applicationContext)
        btService.bluetoothInit()
        if(btService.getTargetDevice(resources.getString(R.string.target_device))!=null){
            btService.connectDevice(btService.getFoundDevice())
        }else{
            //TODO find and bond with device
        }
        if(btService.isConnected())
            verifyDevice()
        listView.setOnItemClickListener { parent, view, position, id ->
            when(text[position]){
                "Addressable LED Strip" -> {
                    val ft = fragmentManager?.beginTransaction()
                    ft?.replace(
                        R.id.contain,
                        LightSettingsFragment(
                            btService
                        )
                    )
                    ft?.addToBackStack("Settings")

                    ft?.commit()

                }
            }
        }
        return view
    }
    private fun verifyDevice() {
        btService.sendData("VER",key)
    }
}