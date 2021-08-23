package com.intive.tmdbandroid.di

import com.intive.tmdbandroid.datasource.MockDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class MockModule {

    @Singleton
    @Provides
    fun provideEventAPIService() : MockDataSource {
        return MockDataSource()
    }
}
