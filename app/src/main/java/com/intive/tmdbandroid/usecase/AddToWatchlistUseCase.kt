package com.intive.tmdbandroid.usecase

import com.intive.tmdbandroid.entity.TVShowORMEntity
import com.intive.tmdbandroid.repository.WatchlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class AddToWatchlistUseCase @Inject constructor(
    private val repository: WatchlistRepository
) {
    suspend fun addToWatchlist(id: String, tvShowORMEntity: TVShowORMEntity): Flow<Boolean> {
        val isExist: List<TVShowORMEntity> = repository.existAsFavorite(id)
        return if (isExist.isEmpty()) {
            repository.insertFavorite(tvShowORMEntity)
            flowOf(true)
        } else
            flowOf(false)
    }
}