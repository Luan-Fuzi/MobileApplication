package com.example.nasaphotos.data

import com.google.gson.annotations.SerializedName

data class GalleryItem (
    var id: String = "",
    @SerializedName("img_src")var src: String = ""
)