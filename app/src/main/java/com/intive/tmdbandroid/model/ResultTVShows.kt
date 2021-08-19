package com.intive.tmdbandroid.model

data class ResultTVShows(
    val page: Int,
    val TVShows: List<TVShow>,
    val total_pages: Int,
    val total_results: Int
)