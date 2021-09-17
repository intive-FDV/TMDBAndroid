package com.intive.tmdbandroid.usecase

import com.intive.tmdbandroid.entity.TVShowORMEntity
import com.intive.tmdbandroid.repository.CatalogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class RemoveTVShowFromWatchlistUseCase @Inject constructor(private val catalogRepository: CatalogRepository) {

    suspend operator fun invoke(tvShow: TVShowORMEntity): Flow<Boolean> {
        catalogRepository.delete(tvShow)
        return flowOf(false)
    }

}