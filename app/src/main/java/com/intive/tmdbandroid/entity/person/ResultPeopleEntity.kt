package com.intive.tmdbandroid.entity.person

import com.intive.tmdbandroid.model.Screening

data class ResultPeopleEntity(
    val page: Int,
    val results: List<PersonItemEntity>,
    val total_pages: Int,
    val total_results: Int
)

data class PersonItemEntity(
    val profile_path: String?,
    val id: Int,
    val name: String,
    val popularity: Double
) {
    fun toScreening(): Screening {
        return Screening(
            backdrop_path = null,
            genres = null,
            id,
            name,
            number_of_episodes = null,
            number_of_seasons = null,
            overview = "",
            poster_path = profile_path,
            status = null,
            vote_average = 0.0,
            popularity,
            release_date = null,
            media_type = "person",
            adult = false,
            genre_ids = null,
            video = false,
            networks = emptyList(),
            my_favorite = false,
            my_rate = 0.0
        )
    }
}
