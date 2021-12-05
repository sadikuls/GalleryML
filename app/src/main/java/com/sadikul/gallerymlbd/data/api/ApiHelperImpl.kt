package com.pactice.hild_mvvm_room.dada.api
import com.sadikul.gallerymlbd.data.model.GalleryItem
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService): ApiHelper{

    override suspend fun getImages(pageNumber: Int, limit: Int): Response<List<GalleryItem>> =
        apiService.getImages(pageNumber, limit)
}