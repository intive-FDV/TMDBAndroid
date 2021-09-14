package com.intive.tmdbandroid.di

import android.app.Application
import com.intive.tmdbandroid.datasource.local.Dao
import com.intive.tmdbandroid.datasource.local.LocalStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalStorageModule {

    @Singleton
    @Provides
    fun getDB(context: Application): LocalStorage {
        return LocalStorage.getDB(context)
    }

    @Singleton
    @Provides
    fun getDao(localStorage: LocalStorage): Dao {
        return localStorage.tvShowDao()
    }
}