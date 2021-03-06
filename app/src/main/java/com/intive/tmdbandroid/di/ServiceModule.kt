package com.intive.tmdbandroid.di

import com.intive.tmdbandroid.datasource.network.Service
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ServiceModule {
    @Singleton
    @Provides
    fun provideEventAPIService() : Service {
        return Service()
    }
}