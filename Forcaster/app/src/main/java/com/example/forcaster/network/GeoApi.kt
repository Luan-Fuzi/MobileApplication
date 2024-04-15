package com.example.forcaster.network

import retrofit2.http.GET
import retrofit2.http.Query

interface GeoApi {
    @GET("v2/city/lookup")
    suspend fun getCity(@Query("location")location:String,@Query("key")key:String): Citys

}