package com.intive.tmdbandroid.entity

import com.intive.tmdbandroid.model.Movie

data class ResultMovies(
    val page: Int,
    val movies: List<Movie>,
    val total_pages: Int,
    val total_results: Int
)