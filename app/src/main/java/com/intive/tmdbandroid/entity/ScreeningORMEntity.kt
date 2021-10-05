package com.intive.tmdbandroid.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.intive.tmdbandroid.model.Genre
import com.intive.tmdbandroid.model.Network
import com.intive.tmdbandroid.model.Screening
import com.intive.tmdbandroid.model.converter.GenreConverter
import com.intive.tmdbandroid.model.converter.IntConverter
import com.intive.tmdbandroid.model.converter.NetworkConverter

@Entity
data class ScreeningORMEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val backdrop_path: String?,
    @TypeConverters(GenreConverter::class)
    val genres: List<Genre>?,
    val name: String,
    val number_of_episodes: Int?,
    val number_of_seasons: Int?,
    val overview: String,
    val poster_path: String?,
    val status: String?,
    val vote_average: Double,
    val popularity: Double,
    val release_date: String?,
    val media_type: String,
    val adult: Boolean,
    @TypeConverters(IntConverter::class)
    val genre_ids: List<Int>?,
    val video: Boolean,
    @TypeConverters(NetworkConverter::class)
    val networks: List<Network>
) {
    fun toScreening(): Screening {
        return Screening(
            backdrop_path,
            genres,
            id,
            name,
            number_of_episodes,
            number_of_seasons,
            overview,
            poster_path,
            status,
            vote_average,
            popularity,
            release_date,
            media_type,
            adult,
            genre_ids,
            video,
            networks
        )
    }
}
