package com.cubanstudio.smartlight.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.cubanstudio.smartlight.MainActivity
import com.cubanstudio.smartlight.R
import com.cubanstudio.smartlight.Routine
import com.cubanstudio.smartlight.SocketClient.SocketClient
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox

class PresetCreateFragment(var parent: PresetsFragment) : Fragment() {
    var socket =SocketClient()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        socket = (activity as MainActivity).socketClient
        var view = inflater.inflate(R.layout.preset_create_fragment, container, false)
        var delaypicker =view.findViewById<NumberPicker>(R.id.turnoffdelazpicker)
        delaypicker.minValue = 1
        delaypicker.maxValue =30
        var effects = arrayListOf<String>("Sunrise","Forrest")
        var adapter = ArrayAdapter<String>(context!!,R.layout.dropdown_menu,effects)
        var label  = view.findViewById<TextView>(R.id.presetLabel)
        var time = view.findViewById<TimePicker>(R.id.timePicker)
        var effect  = view.findViewById<AutoCompleteTextView>(R.id.effectdropdown)
        effect.setAdapter(adapter)
        var mon  = view.findViewById<MaterialCheckBox>(R.id.monBox)
        var tue  = view.findViewById<MaterialCheckBox>(R.id.tueBox)
        var wed  = view.findViewById<MaterialCheckBox>(R.id.wedBox)
        var thu  = view.findViewById<MaterialCheckBox>(R.id.thuBox)
        var fri  = view.findViewById<MaterialCheckBox>(R.id.friBox)
        var sat  = view.findViewById<MaterialCheckBox>(R.id.satBox)
        var sun  = view.findViewById<MaterialCheckBox>(R.id.sunBox)





        view.findViewById<MaterialButton>(R.id.addpresetButton).setOnClickListener {
            val list =arrayListOf<Boolean>(mon.isChecked,tue.isChecked,wed.isChecked,thu.isChecked,fri.isChecked,sat.isChecked,sun.isChecked)
            var routine = Routine(
                label.text.toString(),
                String.format("%d:%02d", time.hour, time.minute),
                true,
                list,
                effect.text.toString()
            )
            var days = ""
            for(b in list){
                if(b)
                    days+="1"
                else
                    days+="0"
            }

            socket.sendMessage("ALARM",String.format("%03d",parent.routines.size),
                String.format("%03d|%03d",time.hour,time.minute),String.format("%03d",effects.indexOf(effect.text.toString())),
                String.format("%03d",delaypicker.value),String.format("%03d",Integer.parseInt(days,2)))
            parent.routines.add(routine)
            parent.adapter.notifyDataSetChanged()
            fragmentManager?.popBackStack()
        }

        return view
    }
}