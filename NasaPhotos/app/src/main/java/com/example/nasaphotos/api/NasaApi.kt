package com.example.nasaphotos.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface NasaApi {

    @GET("photos?sol=1000&api_key=DEMO_KEY")
    fun fetchPhotos(): Call<NasaResponse>

    @GET
    fun fetchUrlBytes(@Url url: String): Call<ResponseBody>
}