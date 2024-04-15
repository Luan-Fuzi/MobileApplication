package com.example.forcaster.network

import com.example.forcaster.database.Weather

data class Weather7d(
    val code: String,
    val daily: List<Weather>,
    val fxLink: String,
    val updateTime: String
)
