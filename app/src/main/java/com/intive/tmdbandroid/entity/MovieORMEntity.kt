package com.intive.tmdbandroid.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.intive.tmdbandroid.model.Genre
import com.intive.tmdbandroid.model.Movie
import com.intive.tmdbandroid.model.converter.GenreConverter

@Entity
data class MovieORMEntity(
    @ColumnInfo(defaultValue = "")
    val backdrop_path: String,
    @TypeConverters(GenreConverter::class)
    val genres: List<Genre> = emptyList(),
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    @ColumnInfo(defaultValue = "")
    val original_title: String,
    @ColumnInfo(defaultValue = "")
    val overview: String,
    val popularity: Double = 0.0,
    @ColumnInfo(defaultValue = "")
    val poster_path: String,
    @ColumnInfo(defaultValue = "")
    val release_date: String?,
    @ColumnInfo(defaultValue = "")
    val title: String,
    val vote_average: Double = 0.0,
    val vote_count: Int = 0,
    @ColumnInfo(defaultValue = "")
    val status: String
) {
    fun toMovie(): Movie {
        return Movie(
            backdrop_path,
            genres,
            id,
            original_title,
            overview,
            popularity,
            poster_path,
            release_date,
            title,
            vote_average,
            vote_count,
            status
        )
    }
}