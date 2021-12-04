package com.mindorks.framework.mvvm.ui.main.viewmodel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.sadikul.gallerymlbd.data.model.GalleryItem
import com.sadikul.gallerymlbd.data.repository.Repository
import com.sadikul.gallerymlbd.utils.NetworkHelper
import com.sadikul.gallerymlbd.utils.Resource
import kotlinx.coroutines.launch

class GalleryViewModel @ViewModelInject constructor(
    private val mRepo: Repository,
    private val mNetworkHelper: NetworkHelper
) : ViewModel() {
    private val TAG = GalleryViewModel:: class.simpleName
    private val _images = MutableLiveData<Resource<List<GalleryItem>>>()
    val images: LiveData<Resource<List<GalleryItem>>>
        get() = _images

    init {
        Log.d(TAG,"$TAG gallery-app init block of viewmodel")
        fetchUsers()
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            _images.postValue(Resource.loading(null))
            if (mNetworkHelper.isNetworkConnected()) {
                mRepo.getImages().let {
                    if (it.isSuccessful) {
                        Log.d(TAG,"$TAG networking network call successfull")
                        _images.postValue(Resource.success(it.body()))
                    } else {
                        Log.d(TAG,"$TAG networking network call not successfull")
                        _images.postValue(Resource.error(it.errorBody().toString(), null))
                    }
                }
            } else{
                Log.d(TAG,"$TAG networking No internet connection")
                _images.postValue(Resource.error("No internet connection", null))
            }
        }
    }
}