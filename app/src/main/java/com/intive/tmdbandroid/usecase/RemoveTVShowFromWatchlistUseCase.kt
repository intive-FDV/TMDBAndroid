package com.intive.tmdbandroid.usecase

import com.intive.tmdbandroid.entity.TVShowORMEntity
import com.intive.tmdbandroid.repository.CatalogRepository
import com.intive.tmdbandroid.repository.WatchlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class RemoveTVShowFromWatchlistUseCase @Inject constructor(private val watchlistRepository: WatchlistRepository) {

    suspend operator fun invoke(tvShow: TVShowORMEntity): Flow<Boolean> {
        watchlistRepository.delete(tvShow)
        return flowOf(false)
    }

}