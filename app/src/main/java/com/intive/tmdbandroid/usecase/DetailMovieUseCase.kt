package com.intive.tmdbandroid.usecase

import com.intive.tmdbandroid.model.Movie
import com.intive.tmdbandroid.repository.CatalogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DetailMovieUseCase @Inject constructor(private val repository: CatalogRepository) {
    operator fun invoke(id: Int): Flow<Movie> = repository.getMovieByID(id)
}