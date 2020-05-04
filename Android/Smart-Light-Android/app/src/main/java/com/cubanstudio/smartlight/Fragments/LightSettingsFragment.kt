package com.cubanstudio.smartlight.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.cubanstudio.smartlight.Bluetooth.BluetoothService
import com.cubanstudio.smartlight.R
import com.google.android.material.button.MaterialButton

class LightSettingsFragment(var btService: BluetoothService) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.light_settings_fragment, container, false)
        view.findViewById<TextView>(R.id.ledType).setOnClickListener {
            var popupMenu = PopupMenu(context!!.applicationContext,view)
            popupMenu.menuInflater.inflate(R.menu.led_menu,popupMenu.menu)
            popupMenu.show()
        }
        view.findViewById<MaterialButton>(R.id.continues).setOnClickListener {
            val ft = fragmentManager?.beginTransaction()
            ft?.replace(
                R.id.contain,
                WifiListFragment(btService)
            )
            ft?.addToBackStack("Settings")
            ft?.commit()

        }
        return view
    }
}