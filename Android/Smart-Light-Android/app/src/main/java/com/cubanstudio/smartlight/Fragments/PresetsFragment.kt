package com.cubanstudio.smartlight.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.cubanstudio.smartlight.Adapters.RoutinesAdapter
import com.cubanstudio.smartlight.R
import com.cubanstudio.smartlight.Routine
import com.google.android.material.button.MaterialButton

class PresetsFragment :Fragment(){
    public var routines = ArrayList<Routine>()
    lateinit var adapter: RoutinesAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var view = inflater.inflate(R.layout.presets_fragment, container, false)
        view.findViewById<MaterialButton>(R.id.addRoutine).setOnClickListener {

            val ft = fragmentManager?.beginTransaction()
            ft?.replace(
                R.id.contain,
                PresetCreateFragment(this)
            )
            ft?.addToBackStack("Presets")
            ft?.commit()
        }
        routines.sortBy { it.time }
        adapter = RoutinesAdapter(
            context!!.applicationContext,
            routines
        )

        val list = view.findViewById<ListView>(R.id.routinesList)
        list.adapter = adapter
       // routines.add(Routine("Alarm","6:00",true, arrayListOf(true,true,true,true,true,true,true)))
        adapter.notifyDataSetChanged()


        return view
    }
}