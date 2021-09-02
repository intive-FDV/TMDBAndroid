package com.intive.tmdbandroid.usecase

import com.intive.tmdbandroid.repository.CatalogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchUseCase @Inject constructor(private val catalogRepository: CatalogRepository) {

    operator fun invoke(): Flow<Any> = catalogRepository.search()
}