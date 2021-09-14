package com.intive.tmdbandroid.usecase

import com.intive.tmdbandroid.entity.TVShowORMEntity
import com.intive.tmdbandroid.repository.WatchlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class DeleteFromWatchlistUseCase @Inject constructor(
    private val repository: WatchlistRepository
) {
    suspend fun deleteFavorite(id: String): Flow<Boolean> {
        val isExist: List<TVShowORMEntity> = repository.existAsFavorite(id)
        return if (isExist.isNotEmpty()) {
            repository.deleteFavorite(isExist.first())
            flowOf(true)
        } else flowOf(false)
    }
}