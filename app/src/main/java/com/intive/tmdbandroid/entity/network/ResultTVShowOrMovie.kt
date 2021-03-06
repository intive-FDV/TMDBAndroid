package com.intive.tmdbandroid.entity.network

import com.intive.tmdbandroid.model.Screening

data class ResultListTVShowOrMovies(
    val page: Int,
    val results: List<ResultTVShowOrMovie>,
    val total_pages: Int,
    val total_results: Int
)

data class ResultTVShowOrMovie(
    val media_type: String,
    val adult: Boolean,
    val genre_ids: List<Int>,
    val id: Int,
    val overview: String?,
    val popularity: Double,
    val release_date: String?,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val backdrop_path: String?,
    val first_air_date: String?,
    val name: String?,
    val poster_path: String?,
    val my_rate: Double,
    val my_favorite: Boolean,
    val profile_path: String?,
) {
    fun toScreening(): Screening {
        return Screening(
            backdrop_path,
            null,
            id,
            name = name ?: title,
            null,
            null,
            overview ?: "",
            poster_path ?: profile_path,
            null,
            vote_average,
            popularity,
            release_date = release_date ?: first_air_date,
            media_type,
            adult,
            genre_ids,
            video,
            emptyList(),
            my_rate,
            my_favorite
        )
    }
}