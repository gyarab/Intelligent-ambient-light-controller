package com.cubanstudio.smartlight.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.cubanstudio.smartlight.R

class DeviceAdapter(private val context: Context,
                    private val dataSource: ArrayList<String>) : BaseAdapter(){
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
        val root = inflater.inflate(R.layout.device_item,parent,false)
        val text = root.findViewById<TextView>(R.id.stripName) as TextView
        text.text = dataSource[position]
        return root
    }

}