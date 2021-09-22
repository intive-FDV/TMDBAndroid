package com.intive.tmdbandroid.entity

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
    val original_language: String,
    val original_title: String?,
    val overview: String,
    val popularity: Double,
    val release_date: String?,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int,
    val backdrop_path: String?,
    val first_air_date: String?,
    val name: String,
    val original_name: String?,
    val poster_path: String?,
)