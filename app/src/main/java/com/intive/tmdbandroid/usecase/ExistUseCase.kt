package com.intive.tmdbandroid.usecase

import com.intive.tmdbandroid.repository.WatchlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ExistUseCase @Inject constructor(private val watchlistRepository: WatchlistRepository) {

    suspend operator fun invoke(id: Int): Flow<Boolean> {
        return flowOf(watchlistRepository.exist(id))
    }

}