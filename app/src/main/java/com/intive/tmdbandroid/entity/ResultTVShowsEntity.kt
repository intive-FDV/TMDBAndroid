package com.intive.tmdbandroid.entity

import com.google.gson.annotations.SerializedName
import com.intive.tmdbandroid.model.Screening

data class ResultTVShowsEntity(
    val page: Int,
    @SerializedName("results")
    val TVShows: List<TVShowListItemEntity>,
    val total_pages: Int,
    val total_results: Int
) {
    fun toScreeningList() : List<Screening> {
        val screeningList: ArrayList<Screening> = arrayListOf()
        return TVShows.mapTo(screeningList,{it.toScreening()})
    }
}