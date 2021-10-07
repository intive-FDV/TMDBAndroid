package com.intive.tmdbandroid.entity.movie

import com.google.gson.annotations.SerializedName
import com.intive.tmdbandroid.model.Screening

data class ResultMoviesEntity(
    val page: Int,
    @SerializedName("results")
    val list: List<MovieListItemEntity>,
    val total_pages: Int,
    val total_results: Int
) {
    fun toScreeningList() : List<Screening> {
        val screeningList: ArrayList<Screening> = arrayListOf()
        return list.mapTo(screeningList, { it.toScreening() })
    }
}