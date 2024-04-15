package com.example.nasaphotos.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.nasaphotos.api.NasaFetcher

class PhotoViewModel : ViewModel(){

    val galleryItemLiveData: LiveData<List<GalleryItem>>

    init{
        galleryItemLiveData = NasaFetcher().fetchPhotos()
    }
}