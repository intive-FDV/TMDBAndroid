package com.intive.tmdbandroid.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.intive.tmdbandroid.entity.TVShowORMEntity
import com.intive.tmdbandroid.model.converter.CreatedByConverter
import com.intive.tmdbandroid.model.converter.GenreConverter

@Database(entities = [(TVShowORMEntity::class)], version = 1, exportSchema = false)
@TypeConverters(CreatedByConverter::class,GenreConverter::class)
abstract class LocalStorage : RoomDatabase() {
    abstract fun tvShowDao(): Dao
}