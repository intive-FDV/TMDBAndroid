package com.intive.tmdbandroid.usecase

import androidx.paging.PagingData
import com.intive.tmdbandroid.model.Screening
import com.intive.tmdbandroid.repository.CatalogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PaginatedPopularTVShowsUseCase @Inject constructor(private val catalogRepository: CatalogRepository) {
    operator fun invoke(): Flow<PagingData<Screening>> = catalogRepository.paginatedPopularTVShows()
}