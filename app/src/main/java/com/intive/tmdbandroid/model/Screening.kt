package com.intive.tmdbandroid.model

import com.intive.tmdbandroid.entity.ScreeningORMEntity

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
    val vote_count: Int,
    val popularity: Double,
    val release_date: String?,
    val media_type: String,
    val adult: Boolean,
    val genre_ids: List<Int>?,
    val video: Boolean,
    val networks: List<Network>,
    var my_rate: Double,
    var my_favorite: Boolean
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
            vote_count,
            popularity,
            release_date,
            media_type,
            adult,
            genre_ids,
            video,
            networks,
            my_rate,
            my_favorite

        )
    }
}
