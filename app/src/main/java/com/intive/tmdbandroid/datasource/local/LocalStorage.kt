package com.intive.tmdbandroid.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.intive.tmdbandroid.entity.MovieORMEntity
import com.intive.tmdbandroid.entity.TVShowORMEntity
import com.intive.tmdbandroid.model.converter.CreatedByConverter
import com.intive.tmdbandroid.model.converter.GenreConverter

@Database(
    entities = [(TVShowORMEntity::class),(MovieORMEntity::class)],
    version = 2,
    exportSchema = true,
)
@TypeConverters(CreatedByConverter::class, GenreConverter::class)
abstract class LocalStorage : RoomDatabase() {

    object MIGRATION_1_2 : Migration(1,2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            val createMovieTable = "CREATE TABLE IF NOT EXISTS `MovieORMEntity` (`backdrop_path` TEXT NOT NULL DEFAULT '', `genres` TEXT NOT NULL, `id` INTEGER NOT NULL, `original_title` TEXT NOT NULL DEFAULT '', `overview` TEXT NOT NULL DEFAULT '', `popularity` REAL NOT NULL, `poster_path` TEXT NOT NULL DEFAULT '', `release_date` TEXT DEFAULT '', `title` TEXT NOT NULL DEFAULT '', `vote_average` REAL NOT NULL, `vote_count` INTEGER NOT NULL, `status` TEXT NOT NULL DEFAULT '', PRIMARY KEY(`id`))"
            database.execSQL(createMovieTable)
        }

    }

    abstract fun tvShowDao(): Dao
}