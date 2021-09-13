package com.intive.tmdbandroid.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.intive.tmdbandroid.model.CreatedBy
import com.intive.tmdbandroid.model.Genre
import com.intive.tmdbandroid.model.TVShow
import com.intive.tmdbandroid.model.converter.CreatedByConverter

@Entity
data class TVShowORMEntity(
    val backdrop_path: String?,
    @TypeConverters(CreatedByConverter::class)
    val created_by: List<CreatedBy>,
    val first_air_date: String?,
    val genres: List<Genre>,
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val last_air_date: String?,
    val name: String,
    val number_of_episodes: Int?,
    val number_of_seasons: Int?,
    val original_name: String,
    val overview: String,
    val poster_path: String?,
    val status: String?,
    val vote_average: Double,
    val vote_count: Int
) {
    fun toTVShow() : TVShow {
        return TVShow(backdrop_path, emptyList(), first_air_date, emptyList(), id, last_air_date, name,
            number_of_episodes, number_of_seasons, original_name, overview, poster_path, status,
            vote_average, vote_count)
    }
}