package com.intive.tmdbandroid.entity.tvshow

import com.intive.tmdbandroid.model.Screening

data class TVShowListItemEntity(
    val backdrop_path: String?,
    val first_air_date: String?,
    val id: Int,
    val name: String,
    val overview: String,
    val popularity: Double?,
    val poster_path: String?,
    val vote_average: Double,
    val my_rate: Double,
    val my_favorite: Boolean
) {
    fun toScreening(): Screening {
        return Screening(
            backdrop_path,
            null,
            id,
            name,
            null,
            null,
            overview,
            poster_path,
            null,
            vote_average,
            popularity ?: 0.0,
            first_air_date,
            "tv",
            false,
            null,
            false,
            emptyList(),
            my_rate,
            my_favorite
        )
    }
}