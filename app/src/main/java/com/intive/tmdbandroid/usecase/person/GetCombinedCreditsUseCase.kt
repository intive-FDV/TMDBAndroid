package com.intive.tmdbandroid.usecase.person

import com.intive.tmdbandroid.model.Screening
import com.intive.tmdbandroid.repository.CatalogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCombinedCreditsUseCase @Inject constructor(private val repository: CatalogRepository) {
    operator fun invoke(personID: Int): Flow<List<Screening>> =
        repository.getCombinedCredits(personID)
}