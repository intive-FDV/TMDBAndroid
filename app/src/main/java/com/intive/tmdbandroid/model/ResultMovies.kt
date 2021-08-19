package com.intive.tmdbandroid.model

data class ResultMovies(
    val page: Int,
    val movies: List<Movie>,
    val total_pages: Int,
    val total_results: Int
)