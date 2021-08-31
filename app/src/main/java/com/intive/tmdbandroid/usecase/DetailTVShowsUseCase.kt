package com.intive.tmdbandroid.usecase

import com.intive.tmdbandroid.model.TVShow
import com.intive.tmdbandroid.repository.CatalogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DetailTVShowsUseCase @Inject constructor(private val showTVRepository: CatalogRepository) {

    operator fun invoke(id:Int): Flow<TVShow> = showTVRepository.getTVShowByID(id)

}