package com.intive.tmdbandroid.entity

import com.intive.tmdbandroid.model.TVShow

data class TVShowListItemEntity(
    val backdrop_path: String?,
    val first_air_date: String?,
    val id: Int,
    val name: String,
    val original_name: String,
    val overview: String,
    val poster_path: String?,
    val vote_average: Double,
    val vote_count: Int
) {
    fun toTVShow() : TVShow {
        return TVShow(backdrop_path, emptyList(), first_air_date, emptyList(), id, null, name,
            null, null, original_name, overview, poster_path, null,
            vote_average, vote_count, emptyList())
    }
}