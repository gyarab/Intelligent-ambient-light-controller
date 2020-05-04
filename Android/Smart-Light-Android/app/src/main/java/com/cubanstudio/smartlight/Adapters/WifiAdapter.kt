package com.cubanstudio.smartlight.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.cubanstudio.smartlight.R
import com.cubanstudio.smartlight.WifiItem

class WifiAdapter(private val context: Context,
                  private val dataSource: ArrayList<WifiItem>) : BaseAdapter(){
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val root = inflater.inflate(R.layout.wifi_list_item,parent,false)
        val wifiName = root.findViewById<TextView>(R.id.wifiName) as TextView
        val wifiMac = root.findViewById<TextView>(R.id.wifiMAC) as TextView
        val signalLevel = root.findViewById<ImageView>(R.id.wifiLevel) as ImageView
        wifiName.text = dataSource[position].wifiName
        wifiMac.text = dataSource[position].wifiMac
        if(dataSource[position].locked){
        when(dataSource[position].wifiStrength){
            in -50..1 -> signalLevel.setImageResource(R.drawable.ic_signal_wifi_4_lock)
            in -70..-50 -> signalLevel.setImageResource(R.drawable.ic_signal_wifi_3_lock)
            in -80..-70 -> signalLevel.setImageResource(R.drawable.ic_signal_wifi_2_lock)
            in -100..-80 -> signalLevel.setImageResource(R.drawable.ic_signal_wifi_1_lock)
            else -> signalLevel.setImageResource(R.drawable.ic_signal_wifi_0)
        }
        }else{
            when(dataSource[position].wifiStrength){
                in -50..1 -> signalLevel.setImageResource(R.drawable.ic_signal_wifi_4)
                in -70..-50 -> signalLevel.setImageResource(R.drawable.ic_signal_wifi_3)
                in -80..-70 -> signalLevel.setImageResource(R.drawable.ic_signal_wifi_2)
                in -100..-80 -> signalLevel.setImageResource(R.drawable.ic_signal_wifi_1)
                else -> signalLevel.setImageResource(R.drawable.ic_signal_wifi_0)
            }
        }

        return root
    }
}