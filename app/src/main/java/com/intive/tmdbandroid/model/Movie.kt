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
) {
    fun toScreening(): Screening {
        return Screening(
            backdrop_path,
            genres,
            id,
            name = title,
            null,
            null,
            overview,
            poster_path,
            status,
            vote_average,
            vote_count,
            popularity,
            release_date,
            media_type = "movie",
            adult = false,
            genre_ids = null,
            false,
            emptyList()
        )
    }
}