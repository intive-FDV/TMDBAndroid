package com.intive.tmdbandroid.usecase

import androidx.paging.PagingData
import com.intive.tmdbandroid.model.Screening
import com.intive.tmdbandroid.repository.CatalogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchUseCase @Inject constructor(private val catalogRepository: CatalogRepository) {
    operator fun invoke(name:String, filterSelected: String?): Flow<PagingData<Screening>> = catalogRepository.searchByName(name, filterSelected)
}