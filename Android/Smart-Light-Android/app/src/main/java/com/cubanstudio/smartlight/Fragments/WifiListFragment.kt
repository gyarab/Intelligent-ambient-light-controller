package com.cubanstudio.smartlight.Fragments

import android.content.Context
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.cubanstudio.smartlight.Adapters.WifiAdapter
import com.cubanstudio.smartlight.Bluetooth.BluetoothService
import com.cubanstudio.smartlight.R
import com.cubanstudio.smartlight.WiFiScanReceiver
import com.cubanstudio.smartlight.WifiItem

class WifiListFragment(private var btService: BluetoothService) : Fragment() {
    var wifiList = ArrayList<WifiItem>()
    lateinit var adapter: WifiAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mWifiManager: WifiManager =
            context?.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val scanReceiver =
            WiFiScanReceiver(mWifiManager, this)
        val intentFilter = IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        context?.registerReceiver(scanReceiver, intentFilter)
        mWifiManager.startScan()

        var results = mWifiManager.scanResults

        results.forEach {
            Log.i("WIFI", it.SSID + " " + it.frequency)
            if (it.frequency < 2500) { // filter out 5ghz networks
                var locked =
                    (it.capabilities.contains("WEP") || it.capabilities.contains("PSK") || it.capabilities.contains(
                        "EAP"
                    ))
                wifiList.add(
                    WifiItem(
                        it.SSID,
                        "",
                        it.level,
                        locked
                    )
                )
            }
        }
        wifiList.sortBy { it.wifiStrength }
        wifiList.reverse()
        var view = inflater.inflate(R.layout.wifi_list_fragment, container, false)


        adapter = WifiAdapter(
            context!!.applicationContext,
            wifiList
        )
        val listView = view.findViewById<ListView>(R.id.wifiListView)
        listView.adapter = adapter
        listView.onItemClickListener = wifiItemClickListener
        view.findViewById<ListView>(R.id.wifiListView).adapter = adapter

        return view
    }

    var wifiItemClickListener =
        AdapterView.OnItemClickListener() { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
            if (wifiList[position].locked) {
                val ft = fragmentManager?.beginTransaction()
                ft?.replace(
                    R.id.contain,
                    WifiLoginFragment(
                        wifiList[position].wifiName,
                        wifiList[position].wifiMac,
                        btService
                    )
                )
                ft?.addToBackStack("Settings")
                ft?.commit()
            } else {
                val ft = fragmentManager?.beginTransaction()
                ft?.replace(
                    R.id.contain,
                    WifiLoginFragment(
                        wifiList[position].wifiName,
                        wifiList[position].wifiMac,
                        btService
                    )
                )
                ft?.addToBackStack("Settings")
                ft?.commit()
            }
        }
}