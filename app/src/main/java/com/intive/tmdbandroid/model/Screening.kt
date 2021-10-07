package com.intive.tmdbandroid.model

import com.intive.tmdbandroid.entity.room.ScreeningORMEntity

data class Screening(
    val backdrop_path: String?,
    val genres: List<Genre>?,
    val id: Int,
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
    val genre_ids: List<Int>?,
    val video: Boolean,
    val networks: List<Network>
) {
    fun toScreeningORMEntity(): ScreeningORMEntity {
        return ScreeningORMEntity(
            id,
            backdrop_path,
            genres,
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
