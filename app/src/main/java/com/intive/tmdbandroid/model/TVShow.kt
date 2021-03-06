package com.intive.tmdbandroid.model

data class TVShow(
    val backdrop_path: String?,
    val first_air_date: String?,
    val genres: List<Genre>,
    val id: Int,
    val last_air_date: String?,
    val name: String,
    val number_of_episodes: Int?,
    val number_of_seasons: Int?,
    val overview: String,
    val poster_path: String?,
    val status: String?,
    val vote_average: Double,
    val networks: List<Network>,
    val my_rate:Double,
    val my_favorite: Boolean
) {
    fun toScreening(): Screening {
        return Screening(
            backdrop_path,
            genres,
            id,
            name,
            number_of_episodes,
            number_of_seasons,
            overview,
            poster_path,
            status,
            vote_average,
            popularity = 0.0,
            release_date = first_air_date,
            media_type = "tv",
            adult = false,
            genre_ids = null,
            video = false,
            networks,
            my_rate,
            my_favorite
        )
    }
}