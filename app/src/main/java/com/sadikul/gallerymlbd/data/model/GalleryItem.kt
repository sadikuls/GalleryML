package com.sadikul.gallerymlbd.data.model
import com.google.gson.annotations.SerializedName

data class GalleryItem(
//{"id":"0","author":"Alejandro Escamilla","width":5616,"height":3744,"url":"https://unsplash.com/photos/yC-Yzbqy7PY","download_url":"https://picsum.photos/id/0/5616/3744"},

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("author")
    val author: String? = null,

    @field:SerializedName("width")
    val width: Int? = null,

    @field:SerializedName("height")
    val height: Int? = null,

    @field:SerializedName("filename")
    val filename: String? = null,

    @field:SerializedName("url")
    val url: String? = null,

    @field:SerializedName("download_url")
    val download_url: String? = null,
)