package com.example.forcaster.network

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "WeatherAccesor"
private const val key = "Go and get your own api plz awa"
class WeatherAccesor {
    private val weatherApi: WeatherApi
    private val geoApi:GeoApi

    init{
        val retrofit = Retrofit.Builder()
            .baseUrl("https://devapi.qweather.com/")
            .addConverterFactory(GsonConverterFactory.create())
//            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        weatherApi = retrofit.create(WeatherApi::class.java)

        val retrofit2 = Retrofit.Builder()
            .baseUrl("https://geoapi.qweather.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        geoApi = retrofit2.create(GeoApi::class.java)
    }


    suspend fun fetchWeatherNow(location:String,unit:String):WeatherNow{
        return weatherApi.fetchWeatherNow(location,key,unit)
    }

    suspend fun fetchWeather7d(location:String,unit:String):Weather7d{
        return weatherApi.fetchWeather7d(location,key,unit)
    }

    suspend fun fetchCity(cityName:String):Citys{
        return geoApi.getCity(cityName,key)
    }
}