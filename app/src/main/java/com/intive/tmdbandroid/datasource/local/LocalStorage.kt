package com.intive.tmdbandroid.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.intive.tmdbandroid.model.TVShow

@Database(entities = [(TVShow::class)], version = 1, exportSchema = false)
abstract class LocalStorage : RoomDatabase() {
    abstract fun tvShowDao(): Dao
}