package com.intive.tmdbandroid.usecase

import com.intive.tmdbandroid.repository.CatalogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetIfExistsUseCase @Inject constructor(private val catalogRepository: CatalogRepository) {

    suspend operator fun invoke(id: Int): Flow<Boolean> {
        val result = catalogRepository.checkIfExistAsFavorite(id)
        return if(result.isEmpty()) flowOf(false)
        else flowOf(true)
    }

}