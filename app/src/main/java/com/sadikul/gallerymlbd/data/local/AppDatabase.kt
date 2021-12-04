package com.sadikul.gallerymlbd.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sadikul.gallerymlbd.data.local.dao.GalleryDao
import com.sadikul.gallerymlbd.data.local.entity.GalleryItemEntity
import com.sadikul.gallerymlbd.utils.Constants.DATABASE_NAME


@Database(entities = [GalleryItemEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    companion object {

        private var appDb: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context) : AppDatabase {
            if(appDb == null) {
                appDb = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                        }
                    })
                    .build()
            }
            return appDb!!
        }
    }

    abstract fun galleryDao(): GalleryDao

}