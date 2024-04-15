package com.example.forcaster.network

import com.google.gson.annotations.SerializedName

data class WeatherNow(
    @SerializedName("code") val code:String,
    @SerializedName("updateTime") val updateTime:String,
    @SerializedName("now") val now:Now
)

data class Now(
    @SerializedName("temp") val temp:String,
    @SerializedName("icon") val icon:String,
    @SerializedName("text") val text:String,
    @SerializedName("windDir") val windDir:String,
    @SerializedName("windScale") val windScale:String,
    @SerializedName("humidity") val humidity:String
)
