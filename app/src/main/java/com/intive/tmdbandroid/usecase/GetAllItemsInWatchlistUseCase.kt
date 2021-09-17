package com.intive.tmdbandroid.usecase

import com.intive.tmdbandroid.model.TVShow
import com.intive.tmdbandroid.repository.WatchlistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllItemsInWatchlistUseCase @Inject constructor(private val watchlistRepository: WatchlistRepository) {

    suspend operator fun invoke(): Flow<List<TVShow>> = watchlistRepository.getFullWatchlist()

}