package com.example.nasaphotos.api

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nasaphotos.data.GalleryItem
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "NasaFetcher"

class NasaFetcher {
    private val nasaApi: NasaApi

    init{
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        nasaApi = retrofit.create(NasaApi::class.java)
    }

    fun fetchPhotos(): LiveData<List<GalleryItem>> {
        val responseLiveData: MutableLiveData<List<GalleryItem>> = MutableLiveData()
        val nasaRequest: Call<NasaResponse> = nasaApi.fetchPhotos()

        nasaRequest.enqueue(object : Callback<NasaResponse> {
            override fun onFailure(call: Call<NasaResponse>, t: Throwable) {
                Log.e(TAG, "Failed to fetch NASA homepage", t)
            }

            override fun onResponse(call: Call<NasaResponse>, response: Response<NasaResponse>) {
                Log.d(TAG, "Response received")
                val nasaResponse: NasaResponse? = response.body()
                var galleryItems: List<GalleryItem> =nasaResponse?.galleryItems
                    ?: mutableListOf()
                galleryItems = galleryItems.filterNot {
                    it.src.isBlank()
                }
                responseLiveData.value = galleryItems


            }
        })
        return responseLiveData
    }

    @WorkerThread
    fun fetchPhoto(url:String): Bitmap?{
        val response:Response<ResponseBody> = nasaApi.fetchUrlBytes(url).execute()
        val bitmap = response.body()?.byteStream()?.use(BitmapFactory::decodeStream)
        Log.i(TAG,"Decoded bitmap=$bitmap from Response=$response")
        return bitmap
    }

}