package com.intive.tmdbandroid.usecase

import com.intive.tmdbandroid.model.ResultTVShows
import com.intive.tmdbandroid.repository.MokkRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PopularTVShowsUseCase @Inject constructor(private val repository: MokkRepository) {

    operator fun invoke(): Flow<ResultTVShows> = repository.mokkPopularTVShows()

}