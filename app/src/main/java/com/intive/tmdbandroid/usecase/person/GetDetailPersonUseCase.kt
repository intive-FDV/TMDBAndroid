package com.intive.tmdbandroid.usecase.person

import com.intive.tmdbandroid.entity.person.ResultPerson
import com.intive.tmdbandroid.repository.CatalogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDetailPersonUseCase @Inject constructor(private val repository: CatalogRepository) {
    operator fun invoke(personID: Int): Flow<ResultPerson> = repository.getDetailPerson(personID)
}