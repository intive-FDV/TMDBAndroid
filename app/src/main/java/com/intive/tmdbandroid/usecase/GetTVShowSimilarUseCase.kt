package com.intive.tmdbandroid.usecase

import com.intive.tmdbandroid.model.Screening
import com.intive.tmdbandroid.repository.CatalogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTVShowSimilarUseCase @Inject constructor(private val repository: CatalogRepository) {
    operator fun invoke(id: Int): Flow<List<Screening>> = repository.getTVShowSimilar(id)
}