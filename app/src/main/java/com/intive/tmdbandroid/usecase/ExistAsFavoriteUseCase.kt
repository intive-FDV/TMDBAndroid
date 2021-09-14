package com.intive.tmdbandroid.usecase

import com.intive.tmdbandroid.entity.TVShowORMEntity
import com.intive.tmdbandroid.repository.WatchlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ExistAsFavoriteUseCase @Inject constructor(
    private val repository: WatchlistRepository
) {

    suspend fun existAsFavorite(id: String): Flow<Boolean> {
        val isExist: List<TVShowORMEntity> = repository.existAsFavorite(id)
        return flowOf(isExist.isNotEmpty())
    }
}