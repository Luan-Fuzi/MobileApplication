package com.example.forcaster

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.forcaster.database.Weather
import com.example.forcaster.database.WeatherDao
import com.example.forcaster.database.WeatherDatabase
import com.example.forcaster.network.Citys
import com.example.forcaster.network.Weather7d
import com.example.forcaster.network.WeatherAccesor
import com.example.forcaster.network.WeatherNow

private const val TAG = "WeatherReppository"

class WeatherRepository {
    private var dao: WeatherDao
    private var database: WeatherDatabase
    private var sharedPrefereces: SharedPreferences

    val weatherAccesor: WeatherAccesor = WeatherAccesor()


    constructor(context: Context) {
        this.database = WeatherDatabase.getInstance(context)
        this.dao = database.weatherDao()
        this.sharedPrefereces = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    }

    fun getCityName(): String {
        val Cityname = sharedPrefereces.getString("city", "暂无城市信息")!!
        Log.d(TAG, "getCityName: $Cityname")
        return Cityname
    }

    fun setCityName(cityName: String) {
        sharedPrefereces.edit().putString("city", cityName).apply()
    }
//纬度经度，用于打开地图
    fun getCityLocationLatLon(): String {
        val lat = sharedPrefereces.getString("lat", "39.9042")!!
        val lon = sharedPrefereces.getString("lon", "116.4074")!!
        return "$lat,$lon"
    }
//经度纬度，用于获取天气
    fun getCityLocationLonLat(): String {
        val lat = sharedPrefereces.getString("lat", "39.9042")!!
        val lon = sharedPrefereces.getString("lon", "116.4074")!!
        return "$lon,$lat"
    }

    fun setCityLocation(lat: String, lon: String) {
        sharedPrefereces.edit().putString("lat", lat).apply()
        sharedPrefereces.edit().putString("lon", lon).apply()
    }

    fun getUnit(): String {
        val unit = sharedPrefereces.getString("unit", "m")!!
        return unit
    }

    fun setUnit(unit: String) {
        sharedPrefereces.edit().putString("unit", unit).apply()
    }

    fun getSend(): Boolean {
        val send = sharedPrefereces.getBoolean("send", false)
        return send
    }

    fun setSend(send: Boolean) {
        sharedPrefereces.edit().putBoolean("send", send).apply()
    }

    //获取当前天气
    suspend fun fetchWeatherNow(): WeatherNow {
        val location = getCityLocationLonLat()
        Log.d(TAG, "fetchWeatherNow: $location")
        val unit = getUnit()
        return weatherAccesor.fetchWeatherNow(location,unit)
    }

    //获取7天天气
    suspend fun fetchWeather7d(): Weather7d {
        val location = getCityLocationLonLat()
        Log.d(TAG, "fetchWeather7d: $location")
        val unit = getUnit()
        return weatherAccesor.fetchWeather7d(location,unit)
    }
//    从本地数据库获取7天天气


    //获取城市信息
    suspend fun fetchCity(): Citys {
        val cityName:String = getCityName()
        return weatherAccesor.fetchCity(cityName)
    }


    suspend fun queryAll() = dao.getAll()

    suspend fun insert(weather: Weather) = dao.insert(weather)

    suspend fun insertBatch(weathers: List<Weather>) = dao.insertBatch(weathers)

    suspend fun deleteAll() = dao.deleteAll()




}