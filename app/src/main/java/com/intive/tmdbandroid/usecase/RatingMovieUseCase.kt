package com.intive.tmdbandroid.usecase

import com.intive.tmdbandroid.repository.CatalogRepository
import javax.inject.Inject

class RatingMovieUseCase @Inject constructor(private val repository: CatalogRepository) {
    suspend operator fun invoke(id: Int, rating: Double): Boolean = repository.setMovieRating(id, rating)
}