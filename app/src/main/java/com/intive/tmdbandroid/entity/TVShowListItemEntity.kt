package com.intive.tmdbandroid.entity

import com.intive.tmdbandroid.model.Screening

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
            0.0,
            first_air_date,
            "tv",
            false,
            null,
            false,
            emptyList()

        )
    }
}