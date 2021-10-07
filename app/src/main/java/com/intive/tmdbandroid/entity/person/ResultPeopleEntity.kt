package com.intive.tmdbandroid.entity.person

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
)
