package com.intive.tmdbandroid.usecase

import com.intive.tmdbandroid.entity.ResultTVShows
import com.intive.tmdbandroid.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PopularTVShowsUseCase @Inject constructor(private val repository: Repository) {

    operator fun invoke(): Flow<ResultTVShows> = repository.popularTVShows()

}