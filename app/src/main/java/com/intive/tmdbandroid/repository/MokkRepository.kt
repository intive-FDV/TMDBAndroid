package com.intive.tmdbandroid.repository

import com.intive.tmdbandroid.datasource.MokkDataSource
import com.intive.tmdbandroid.model.ResultMovies
import com.intive.tmdbandroid.model.ResultTVShows
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MokkRepository @Inject constructor(
    private val dataSource: MokkDataSource,
) {

    fun mokkPopularMovies() : Flow<ResultMovies> {
        return dataSource.mokkPopularMovies()
    }

    fun mokkPopularTVShows() : Flow<ResultTVShows> {
        return dataSource.mokkPopularTVShows()
    }
}
