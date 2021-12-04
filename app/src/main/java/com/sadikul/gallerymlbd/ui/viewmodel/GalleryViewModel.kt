package com.mindorks.framework.mvvm.ui.main.viewmodel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.sadikul.gallerymlbd.data.local.entity.GalleryItemEntity
import com.sadikul.gallerymlbd.data.repository.Repository
import com.sadikul.gallerymlbd.utils.NetworkHelper
import com.sadikul.gallerymlbd.utils.Resource
import kotlinx.coroutines.launch

class GalleryViewModel @ViewModelInject constructor(
    private val repository: Repository
) : ViewModel() {
    private val TAG = GalleryViewModel:: class.simpleName
    private val galleryMutableLiveData = MutableLiveData<Resource<List<GalleryItemEntity>>>()
    val images: LiveData<Resource<List<GalleryItemEntity>>>
        get() = galleryMutableLiveData

    init {
        Log.d(TAG,"$TAG gallery-app init block of viewmodel")
        fetchUsers()
    }

    private fun fetchUsers() {
        viewModelScope.launch {
                repository.getImages(galleryMutableLiveData)
        }
    }
}