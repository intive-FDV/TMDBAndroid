package com.intive.tmdbandroid.usecase

import com.intive.tmdbandroid.model.ResultTVShows
import com.intive.tmdbandroid.repository.MockRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PopularTVShowsUseCase @Inject constructor(private val repository: MockRepository) {

    operator fun invoke(): Flow<ResultTVShows> = repository.mokkPopularTVShows()

}