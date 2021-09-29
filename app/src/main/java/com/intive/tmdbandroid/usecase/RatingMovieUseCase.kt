package com.intive.tmdbandroid.usecase

import com.intive.tmdbandroid.model.Movie
import com.intive.tmdbandroid.repository.CatalogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RatingMovieUseCase @Inject constructor(private val repository: CatalogRepository) {
    suspend operator fun invoke(id: Int, rating: Double): Boolean = repository.setMovieRating(id, rating)
}