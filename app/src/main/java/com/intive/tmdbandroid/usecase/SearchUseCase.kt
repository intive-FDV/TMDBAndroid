package com.intive.tmdbandroid.usecase

import com.intive.tmdbandroid.model.TVShow
import com.intive.tmdbandroid.repository.CatalogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchUseCase @Inject constructor(private val catalogRepository: CatalogRepository) {

    operator fun invoke(name:String): Flow<List<TVShow>> = catalogRepository.search(name)
}