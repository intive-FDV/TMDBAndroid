package com.intive.tmdbandroid.entity

import com.intive.tmdbandroid.model.Screening

data class MovieListItemEntity(
    val backdrop_path: String?,
    val release_date: String?,
    val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String?,
    val vote_average: Double,
) {
    fun toScreening(): Screening {
        return Screening(
            backdrop_path,
            null,
            id,
            title,
            null,
            null,
            overview,
            poster_path,
            null,
            vote_average,
            0.0,
            release_date,
            "tv",
            false,
            null,
            false,
            emptyList()
        )
    }
}