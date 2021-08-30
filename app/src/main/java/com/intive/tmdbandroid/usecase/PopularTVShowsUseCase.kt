package com.intive.tmdbandroid.usecase

import com.intive.tmdbandroid.entity.ResultTVShowsEntity
import com.intive.tmdbandroid.repository.CatalogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PopularTVShowsUseCase @Inject constructor(private val catalogRepository: CatalogRepository) {

    operator fun invoke(): Flow<ResultTVShowsEntity> = catalogRepository.popularTVShows()

}