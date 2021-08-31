package com.intive.tmdbandroid.entity

import com.google.gson.annotations.SerializedName
import com.intive.tmdbandroid.model.TVShow

data class ResultTVShowsEntity(
    val page: Int,
    @SerializedName("results")
    val TVShows: List<TVShowListItemEntity>,
    val total_pages: Int,
    val total_results: Int
) {
    fun toTVShowList() : List<TVShow> {
        val tvShowList : ArrayList<TVShow> = arrayListOf()
        return TVShows.mapTo(tvShowList, { it.toTVShow() })
    }
}