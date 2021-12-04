package com.sadikul.gallerymlbd.data.local.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "gallery")
data class GalleryItemEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    @NonNull
    val id: Int,

    @Nullable
    @ColumnInfo(name = "author")
    val author: String? = null,

    @Nullable
    @ColumnInfo(name = "width")
    val width: Int? = null,

    @Nullable
    @ColumnInfo(name = "height")
    val height: Int? = null,

    @Nullable
    @ColumnInfo(name = "filename")
    val filename: String? = null,

    @Nullable
    @ColumnInfo(name = "url")
    val url: String? = null,

    @Nullable
    @ColumnInfo(name = "download_url")
    val download_url: String? = null
) : Parcelable
