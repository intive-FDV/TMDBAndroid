package com.intive.tmdbandroid.entity

data class ResultCombinedCredits(
    val cast: List<CastItem>
)

data class CastItem(
    val name: String,
    val title: String?,
    val poster_path: String?,
    val backdrop_path: String?,
    val media_type: String,
    val id: Int,
)
