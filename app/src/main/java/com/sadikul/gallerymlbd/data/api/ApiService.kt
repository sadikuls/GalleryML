package com.pactice.hild_mvvm_room.dada.api
import com.sadikul.gallerymlbd.data.model.GalleryItem
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("list")
    suspend fun getImages(): Response<List<GalleryItem>>

}