package com.cubanstudio.smartlight.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.cubanstudio.smartlight.R
import com.cubanstudio.smartlight.Routine
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial

class RoutinesAdapter(private val context: Context,private val routines: ArrayList<Routine>):BaseAdapter() {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val root = inflater.inflate(R.layout.routine_item,parent,false)
        val label = root.findViewById<TextView>(R.id.routinelabel)
        val days = root.findViewById<TextView>(R.id.days)
        val time = root.findViewById<TextView>(R.id.time)

        label.text = routines[position].name
        time.text = routines[position].time

        routines[position].activated = true;

        var text = ""
        for (i in 0..7){
            when(i){
                0-> if (routines[position].days[i])text+= "Mon  "
                1-> if (routines[position].days[i])text+= "Tue  "
                2-> if (routines[position].days[i])text+= "Wed  "
                3-> if (routines[position].days[i])text+= "Thu  "
                4-> if (routines[position].days[i])text+= "Fri  "
                5-> if (routines[position].days[i])text+= "Sat  "
                6-> if (routines[position].days[i])text+= "Sun"
            }
        }
        days.text = text

        return root
    }

    override fun getItem(position: Int): Any {
        return routines[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return routines.size
    }

}