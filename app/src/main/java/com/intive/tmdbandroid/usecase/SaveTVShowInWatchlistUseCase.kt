package com.intive.tmdbandroid.usecase

import com.intive.tmdbandroid.entity.TVShowORMEntity
import com.intive.tmdbandroid.repository.WatchlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class SaveTVShowInWatchlistUseCase @Inject constructor(private val watchlistRepository: WatchlistRepository) {

    suspend operator fun invoke(tvShow: TVShowORMEntity): Flow<Boolean>{
        watchlistRepository.insert(tvShow)
        return flowOf(true)
    }

}