package com.intive.tmdbandroid.usecase

import com.intive.tmdbandroid.entity.ResultMovies
import com.intive.tmdbandroid.repository.MockRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PopularMoviesUseCase @Inject constructor(private val repository: MockRepository) {

    operator fun invoke(): Flow<ResultMovies> = repository.mokkPopularMovies()

}