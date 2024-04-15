package com.example.forcaster.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("v7/weather/7d")
    suspend fun fetchWeather7d(@Query("location")location:String,@Query("key")key:String,@Query("unit")unit:String="m"): Weather7d

    @GET("v7/weather/now")
    suspend fun fetchWeatherNow(@Query("location")location:String,@Query("key")key:String,@Query("unit")unit:String="m"): WeatherNow


}