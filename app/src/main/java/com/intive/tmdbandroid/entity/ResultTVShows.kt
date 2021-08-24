package com.intive.tmdbandroid.entity

import com.google.gson.annotations.SerializedName
import com.intive.tmdbandroid.model.TVShow

data class ResultTVShows(
    val page: Int,
    @SerializedName("results")
    val TVShows: List<TVShow>,
    val total_pages: Int,
    val total_results: Int
)