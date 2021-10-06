package com.intive.tmdbandroid.usecase

import com.intive.tmdbandroid.repository.CatalogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTVShowTrailerUseCase @Inject constructor(private val repository: CatalogRepository) {
    operator fun invoke(id:Int): Flow<String> = repository.getTVShowTrailer(id)
}