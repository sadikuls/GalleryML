package com.sadikul.gallerymlbd.data.repository

import com.pactice.hild_mvvm_room.dada.api.ApiService
import javax.inject.Inject

class Repository @Inject constructor(val apiService: ApiService){

    suspend fun getImages() = apiService.getImages()

}