package com.intive.tmdbandroid.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.intive.tmdbandroid.entity.TVShowORMEntity
import com.intive.tmdbandroid.model.converter.CreatedByConverter
import com.intive.tmdbandroid.model.converter.GenreConverter

@Database(entities = [(TVShowORMEntity::class)], version = 1, exportSchema = false)
@TypeConverters(CreatedByConverter::class, GenreConverter::class)
abstract class LocalStorage : RoomDatabase() {
    abstract fun tvShowDao(): Dao

    companion object {
        private var INSTANCE: LocalStorage? = null

        fun getDB(context: Context): LocalStorage {
            if (INSTANCE == null) {
                synchronized(LocalStorage::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        LocalStorage::class.java, "watchlist.db"
                    )
                        .build()
                }
            }
            return INSTANCE!!
        }
    }
}