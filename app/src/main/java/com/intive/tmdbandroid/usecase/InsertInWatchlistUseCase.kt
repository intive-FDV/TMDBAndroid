package com.intive.tmdbandroid.usecase

import com.intive.tmdbandroid.model.Screening
import com.intive.tmdbandroid.repository.WatchlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class InsertInWatchlistUseCase @Inject constructor(private val watchlistRepository: WatchlistRepository) {

    suspend operator fun invoke(screening: Screening): Flow<Boolean> {
        watchlistRepository.insert(screening)
        return flowOf(true)
    }

}