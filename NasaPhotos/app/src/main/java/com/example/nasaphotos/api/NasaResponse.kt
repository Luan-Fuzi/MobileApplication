package com.example.nasaphotos.api

import com.example.nasaphotos.data.GalleryItem
import com.google.gson.annotations.SerializedName

class NasaResponse {
    @SerializedName("photos")
    lateinit var galleryItems:List<GalleryItem>
}