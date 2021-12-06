package com.sadikul.gallerymlbd.data.local.dao

import androidx.room.*
import com.sadikul.gallerymlbd.data.local.entity.GalleryItemEntity

@Dao
interface GalleryDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: GalleryItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(item: List<GalleryItemEntity>)

    @Query("select * from gallery")
    suspend fun getAllPhotos() : List<GalleryItemEntity>

    @Query("delete from gallery")
    suspend fun clearAll()
}