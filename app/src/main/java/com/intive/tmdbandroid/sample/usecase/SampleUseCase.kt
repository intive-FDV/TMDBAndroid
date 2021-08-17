package com.intive.tmdbandroid.sample.usecase

import com.intive.tmdbandroid.sample.domain.Sample
import com.intive.tmdbandroid.sample.repository.SampleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SampleUseCase @Inject constructor(private val repository: SampleRepository) {

    operator fun invoke(): Flow<Sample> = repository.sample()

}