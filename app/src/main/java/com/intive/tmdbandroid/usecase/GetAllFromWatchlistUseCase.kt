package com.intive.tmdbandroid.usecase

import com.intive.tmdbandroid.entity.TVShowORMEntity
import com.intive.tmdbandroid.repository.WatchlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetAllFromWatchlistUseCase @Inject constructor(
    private val repository: WatchlistRepository
) {
    suspend fun allFavorites(): Flow<List<TVShowORMEntity>> {
        return flowOf(repository.allFavorites())
    }
}