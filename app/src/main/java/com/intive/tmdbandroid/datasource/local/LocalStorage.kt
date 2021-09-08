package com.intive.tmdbandroid.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.intive.tmdbandroid.model.Genre
import com.intive.tmdbandroid.model.TVShow
import com.intive.tmdbandroid.model.converter.CreatedByConverter
import com.intive.tmdbandroid.model.converter.GenreConverter

@Database(entities = [(TVShow::class)], version = 1, exportSchema = false)
@TypeConverters(CreatedByConverter::class,GenreConverter::class)
abstract class LocalStorage : RoomDatabase() {
    abstract fun tvShowDao(): Dao
}