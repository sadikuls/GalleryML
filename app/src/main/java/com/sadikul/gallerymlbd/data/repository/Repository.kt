package com.sadikul.gallerymlbd.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.pactice.hild_mvvm_room.dada.api.ApiService
import com.sadikul.gallerymlbd.data.local.AppDatabase
import com.sadikul.gallerymlbd.data.local.entity.GalleryItemEntity
import com.sadikul.gallerymlbd.data.model.GalleryItem
import com.sadikul.gallerymlbd.utils.NetworkHelper
import com.sadikul.gallerymlbd.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class Repository @Inject constructor(val apiService: ApiService, private val appDatabase: AppDatabase,
                                     private val networkhelper: NetworkHelper
){
    private val TAG = Repository::class.java.simpleName

    suspend fun getImages(pageNumber:Int, limit: Int, imagesLiveData: MutableLiveData<Resource<List<GalleryItemEntity>>>) {
        Log.d(TAG,"Networking getImages() ")
        imagesLiveData.postValue(Resource.loading(null))
        if (networkhelper.isNetworkConnected()) {
            Log.d(TAG,"Networking getImages() network available")
            val serverResponse = apiService.getImages(pageNumber, limit)
            if (serverResponse.isSuccessful){
                val images = processImages(serverResponse.body())
                insertImagesToDb(images, imagesLiveData)
                imagesLiveData.postValue(Resource.success("images from server",images))
            } else getPhotoFromDb(imagesLiveData)
        }else{
            getPhotoFromDb(imagesLiveData)
        }

    }

    private fun processImages(response: List<GalleryItem?>?): List<GalleryItemEntity> {
        Log.d(TAG,"Networking getImages() Processing data")
        val list = mutableListOf<GalleryItemEntity>()
        response?.let {
            for (data in it) {
                data?.apply {
                    val item = GalleryItemEntity(
                        id ?: return@apply,
                        author, width, height, filename, url, download_url
                    )
                    list.add(item)
                }
            }
        }
        return list
    }

    private fun insertImagesToDb(
        galleryList: List<GalleryItemEntity>,
        liveData: MutableLiveData<Resource<List<GalleryItemEntity>>>
    ) {
        Log.d(TAG,"Networking insertImagesToDb() inserting to local db")
        CoroutineScope(Dispatchers.Main).launch {

            if(galleryList.isNotEmpty()){
                val insertionProcessDone = withContext(Dispatchers.IO){
                    try {
                        appDatabase.galleryDao().insertAll(galleryList)
                        true
                    }
                    catch (exp: Exception){
                        true
                    }
                }
                if(insertionProcessDone){
                    //getPhotoFromDb(liveData)
                    Log.d(TAG,"Networking insertImagesToDb successfully inserted all items to db")
                }
            }
            //else getPhotoFromDb(liveData)
        }
    }

    private fun getPhotoFromDb(liveData: MutableLiveData<Resource<List<GalleryItemEntity>>>) {
        Log.d(TAG,"Networking getPhotoFromDb() getting from local db")
        CoroutineScope(Dispatchers.Default).launch {
            try {
                liveData.postValue(Resource.success("data from local-db",appDatabase.galleryDao().getAllPhotos()))
            }catch (exp: Exception){
                liveData.postValue(Resource.error("Error on getting data from db",null))
            }
        }
    }

}