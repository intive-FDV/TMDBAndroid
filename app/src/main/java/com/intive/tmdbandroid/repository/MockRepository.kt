package com.intive.tmdbandroid.repository

import com.intive.tmdbandroid.datasource.MockDataSource
import com.intive.tmdbandroid.entity.ResultMovies
import com.intive.tmdbandroid.entity.ResultTVShows
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockRepository @Inject constructor(
    private val dataSource: MockDataSource,
) {

    fun mokkPopularMovies() : Flow<ResultMovies> {
        return dataSource.mokkPopularMovies()
    }

    fun mokkPopularTVShows() : Flow<ResultTVShows> {
        return dataSource.mokkPopularTVShows()
    }
}
