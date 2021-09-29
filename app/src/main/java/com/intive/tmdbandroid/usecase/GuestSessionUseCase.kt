package com.intive.tmdbandroid.usecase

import com.intive.tmdbandroid.model.Movie
import com.intive.tmdbandroid.model.Session
import com.intive.tmdbandroid.repository.CatalogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GuestSessionUseCase @Inject constructor(private val repository: CatalogRepository) {
    suspend operator fun invoke(): Session = repository.getGuestSession()
}