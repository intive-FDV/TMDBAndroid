package com.intive.tmdbandroid.sample.repository

import com.intive.tmdbandroid.sample.datasource.SampleDataSource
import com.intive.tmdbandroid.sample.model.Sample
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SampleRepository @Inject constructor(
    private val dataSource: SampleDataSource,
) {

    fun sample(): Flow<Sample> {
        return dataSource.sample().map { it.toSample() }
    }

}
