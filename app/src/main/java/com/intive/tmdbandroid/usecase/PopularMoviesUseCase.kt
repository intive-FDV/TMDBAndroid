package com.intive.tmdbandroid.usecase

import com.intive.tmdbandroid.model.ResultMovies
import com.intive.tmdbandroid.repository.MokkRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PopularMoviesUseCase @Inject constructor(private val repository: MokkRepository) {

    operator fun invoke(): Flow<ResultMovies> = repository.mokkPopularMovies()

}