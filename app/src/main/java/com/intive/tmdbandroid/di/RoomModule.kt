package com.intive.tmdbandroid.di

import android.app.Application
import androidx.room.Room
import com.intive.tmdbandroid.common.Constants
import com.intive.tmdbandroid.datasource.local.Dao
import com.intive.tmdbandroid.datasource.local.LocalStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RoomModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): LocalStorage = Room.databaseBuilder(
        app,
        LocalStorage::class.java, Constants.DATABASE
    )
        .fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideDao(
        database: LocalStorage
    ): Dao = database.tvShowDao()

}