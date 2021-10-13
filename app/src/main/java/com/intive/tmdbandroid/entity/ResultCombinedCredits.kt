package com.intive.tmdbandroid.entity

import com.intive.tmdbandroid.model.Screening

data class ResultCombinedCredits(
    val cast: List<CastItem>
)

data class CastItem(
    val name: String,
    val title: String?,
    val backdrop_path: String?,
    val media_type: String,
    val id: Int,
) {
    fun toScreening(): Screening {
        return Screening(
            backdrop_path,
            genres = null,
            id,
            name = title ?: name,
            number_of_episodes = null,
            number_of_seasons = null,
            overview = "",
            poster_path = null,
            status = null,
            vote_average = 0.0,
            vote_count = 0,
            popularity = 0.0,
            release_date = null,
            media_type,
            adult = false,
            genre_ids = null,
            video = false,
            networks = emptyList(),
            my_favorite = false,
            my_rate = 0.0
        )
    }
}
