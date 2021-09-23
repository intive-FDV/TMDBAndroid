package com.intive.tmdbandroid.entity

import com.google.gson.annotations.SerializedName
import com.intive.tmdbandroid.model.Screening
import com.intive.tmdbandroid.model.TVShow

data class ResultTVShowsEntity(
    val page: Int,
    @SerializedName("results")
    val TVShows: List<TVShowListItemEntity>,
    val total_pages: Int,
    val total_results: Int
) {
    fun toScreeningList() : List<Screening> {
        val tvShowList : ArrayList<TVShow> = arrayListOf()
        TVShows.mapTo(tvShowList, { it.toTVShow() })
        val screeningList: ArrayList<Screening> = arrayListOf()
        return tvShowList.mapTo(screeningList,{it.toScreening()})
    }
}