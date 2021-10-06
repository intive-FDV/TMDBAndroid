package com.intive.tmdbandroid.usecase.person

import com.intive.tmdbandroid.entity.ResultCombinedCredits
import com.intive.tmdbandroid.repository.CatalogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCombinedCreditsUseCase @Inject constructor(private val repository: CatalogRepository) {
    operator fun invoke(personID: Int): Flow<ResultCombinedCredits> =
        repository.getCombinedCredits(personID)
}