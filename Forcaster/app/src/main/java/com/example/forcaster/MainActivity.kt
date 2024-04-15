package com.example.forcaster

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import androidx.lifecycle.Observer

import androidx.lifecycle.ViewModelProvider
import com.example.forcaster.database.Weather
import com.example.forcaster.network.WeatherAccesor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.zip.Inflater

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() ,MainFragment.Callbacks{



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_main_pad)
//            val detailFragment = supportFragmentManager.findFragmentById(R.id.detail_container)
//            if (detailFragment == null) {
//                val detailfragment = DetailFragment.newInstance(Weather("1","1","1","1","1","1","1","1","1"))
//                supportFragmentManager
//                    .beginTransaction()
//                    .add(R.id.detail_container, detailfragment)
//                    .commit()
//
//            }
        } else if (this.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_main)
        }


        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment == null) {
            val fragment = MainFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()

        }



    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            setContentView(R.layout.activity_main_pad)
//
//            val currentMainFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
//            if (currentMainFragment == null) {
//                val mainFragment = MainFragment.newInstance()
//                supportFragmentManager
//                    .beginTransaction()
//                    .add(R.id.fragment_container, mainFragment)
//                    .commit()
//            }
//
//            val currentDetailFragment = supportFragmentManager.findFragmentById(R.id.detail_container)
//            if (currentDetailFragment == null) {
//                val detailFragment = DetailFragment.newInstance()
//                supportFragmentManager
//                    .beginTransaction()
//                    .add(R.id.detail_container, detailFragment)
//                    .commit()
//            }
//        } else if (this.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            setContentView(R.layout.activity_main)
//
//            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
//            if (currentFragment == null) {
//                val fragment = MainFragment.newInstance()
//                supportFragmentManager
//                    .beginTransaction()
//                    .add(R.id.fragment_container, fragment)
//                    .commit()
//            }
//        }
//    }

    override fun onWeatherSelected(weather: Weather) {
//        竖屏，直接替换
        if (this.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            val fragment = DetailFragment.newInstance(weather)
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        } else if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            横屏，修改detail_container
            val fragment = DetailFragment.newInstance(weather)
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.detail_container, fragment)
                .commit()
        }
    }




}