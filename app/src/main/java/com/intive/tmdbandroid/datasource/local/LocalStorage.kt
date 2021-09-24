package com.intive.tmdbandroid.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.intive.tmdbandroid.entity.ScreeningORMEntity
import com.intive.tmdbandroid.model.converter.GenreConverter
import com.intive.tmdbandroid.model.converter.IntConverter

@Database(
    entities = [(ScreeningORMEntity::class)],
    version = 1,
    exportSchema = true,
)
@TypeConverters(GenreConverter::class, IntConverter::class)
abstract class LocalStorage : RoomDatabase() {
    abstract fun screeningDao(): Dao
}