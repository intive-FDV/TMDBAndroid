package com.intive.tmdbandroid.usecase

import com.intive.tmdbandroid.model.Session
import com.intive.tmdbandroid.repository.CatalogRepository
import javax.inject.Inject

class RatingMovieUseCase @Inject constructor(private val repository: CatalogRepository) {
    suspend operator fun invoke(id: Int, rating: Double,session: Session): Boolean = repository.setMovieRating(id, rating,session)
}