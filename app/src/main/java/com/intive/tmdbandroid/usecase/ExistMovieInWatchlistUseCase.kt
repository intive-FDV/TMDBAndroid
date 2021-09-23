package com.intive.tmdbandroid.usecase

import com.intive.tmdbandroid.repository.WatchlistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExistMovieInWatchlistUseCase @Inject constructor(private val repository: WatchlistRepository) {

    suspend operator fun invoke(id: Int): Flow<Boolean> = repository.existMovie(id)

}