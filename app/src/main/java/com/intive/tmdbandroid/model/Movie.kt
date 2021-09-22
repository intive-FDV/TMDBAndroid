package com.intive.tmdbandroid.model

data class Movie(
    val backdrop_path: String,
    val genres: List<Genre>,
    val id: Int,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String?,
    val title: String,
    val vote_average: Double,
    val vote_count: Int,
    val status: String
)