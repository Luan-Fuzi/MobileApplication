package com.example.forcaster.network

data class Citys(
    val code: String,
    val location: List<City>
)

data class City(
    val name:String,
    val adam1:String,
    val lat:String,
    val lon:String
)
