package com.pactice.hild_mvvm_room.dada.api
import com.sadikul.gallerymlbd.data.model.GalleryItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("list")
    suspend fun getImages(
        @Query("page") pageNumber: Int,
        @Query("limit") limit: Int
    ): Response<List<GalleryItem>>

}