package com.intive.tmdbandroid.usecase

import com.intive.tmdbandroid.repository.WatchlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetIfExistsUseCase @Inject constructor(private val watchlistRepository: WatchlistRepository) {

    suspend operator fun invoke(id: Int): Flow<Boolean> {
        val result = watchlistRepository.checkIfExistAsFavorite(id)
        return if(result.isEmpty()) flowOf(false)
        else flowOf(true)
    }

}