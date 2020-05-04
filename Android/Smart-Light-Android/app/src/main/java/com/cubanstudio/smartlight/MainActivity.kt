package com.cubanstudio.smartlight

import android.content.Context
import android.os.Bundle
import android.os.StrictMode
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.cubanstudio.smartlight.Fragments.HomeFragment
import com.cubanstudio.smartlight.Fragments.PresetsFragment
import com.cubanstudio.smartlight.SocketClient.SocketClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_screen_slide.*
import org.json.JSONArray
import org.json.JSONObject

private const val NUM_PAGES = 2

class MainActivity : FragmentActivity() {
    public lateinit var socketClient: SocketClient
    public var x = ""
    private lateinit var mPager: ViewPager
    val navigationlistener = BottomNavigationView.OnNavigationItemSelectedListener {
        when(it.itemId){
            R.id.navigation_home -> {
                mPager.currentItem = 1
                true
            }
            R.id.navigation_routines -> {
                mPager.currentItem = 0
                true
            }

            else -> false
        }
    }





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_slide)
        // or add <item name="android:windowTranslucentStatus">true</item> in the theme
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        var policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val attrib = window.attributes

        attrib.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        // Instantiate a ViewPager and a PagerAdapter.

        mPager = findViewById(R.id.pager)

        val sharedPref = getSharedPreferences(
            getString(R.string.IPadress), Context.MODE_PRIVATE)
        val ip = sharedPref.getString("IP",null)

        socketClient = SocketClient()
        socketClient.initSocket()

        if(ip != "" && !ip.isNullOrEmpty()){
            socketClient.addDevice(ip,9003)
        }





        val pagerAdapter = ScreenSlidePagerAdapter(supportFragmentManager)

        mPager.adapter = pagerAdapter
        mPager.setPageTransformer(true,ZoomOutTransformer())
        mPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                when(position){
                    0 -> navbar.menu.findItem(R.id.navigation_routines).isChecked = true
                    1 -> navbar.menu.findItem(R.id.navigation_home).isChecked = true


                }
            }
        })
        navbar.setOnNavigationItemSelectedListener(navigationlistener)
        navbar.menu.findItem(R.id.navigation_home).isChecked = true
        mPager.currentItem = 1

    }




    override fun onBackPressed() {
        if (mPager.currentItem == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed()
        } else {
            // Otherwise, select the previous step.
            mPager.currentItem = mPager.currentItem - 1
        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private inner class ScreenSlidePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        override fun getCount(): Int = NUM_PAGES

        override fun getItem(position: Int): Fragment{

            when(position){
                1 -> {

                    return HomeFragment()
                }
                0 -> {return PresetsFragment()
                }




            }

            return HomeFragment()
        }
    }



}

