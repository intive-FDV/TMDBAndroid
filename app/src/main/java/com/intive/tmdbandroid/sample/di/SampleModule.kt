package com.intive.tmdbandroid.sample.di

import com.intive.tmdbandroid.sample.datasource.SampleDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class SampleModule {

    @Singleton
    @Provides
    fun provideSampleDataSource(): SampleDataSource {
        return SampleDataSource()
    }
}
