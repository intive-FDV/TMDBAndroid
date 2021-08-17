package com.intive.tmdbandroid.sample.datasource

import com.intive.tmdbandroid.sample.entity.SampleEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.concurrent.TimeUnit

class SampleDataSource {
    fun sample(): Flow<SampleEntity> {
        return flow {
            delay(TimeUnit.SECONDS.toMillis(3))
            emit(SampleEntity(dummy = "Hello World!"))
        }
    }
}