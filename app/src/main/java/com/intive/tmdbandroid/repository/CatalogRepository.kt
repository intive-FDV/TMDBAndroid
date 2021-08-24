package com.intive.tmdbandroid.repository

import com.intive.tmdbandroid.datasource.network.Service
import com.intive.tmdbandroid.entity.ResultTVShows
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatalogRepository @Inject constructor(
    private val service: Service,
) {
    fun popularTVShows() : Flow<ResultTVShows> {
        return service.getPopularTVShows()
    }
}