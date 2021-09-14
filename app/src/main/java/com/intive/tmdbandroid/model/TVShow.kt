package com.intive.tmdbandroid.model

import com.intive.tmdbandroid.entity.TVShowORMEntity

data class TVShow(

    val backdrop_path: String?,
    val created_by: List<CreatedBy>,
    val first_air_date: String?,
    val genres: List<Genre>,
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
    fun toTVShowORMEntity(): TVShowORMEntity {
        return TVShowORMEntity(
            backdrop_path,
            created_by,
            first_air_date,
            genres,
            id,
            last_air_date,
            name,
            number_of_episodes,
            number_of_seasons,
            original_name,
            overview,
            poster_path,
            status,
            vote_average,
            vote_count
        )
    }
}