package com.pactice.hild_mvvm_room.dada.api
import com.sadikul.gallerymlbd.data.model.GalleryItem
import retrofit2.Response

interface ApiHelper {
    suspend fun getImages(): Response<List<GalleryItem>>
}