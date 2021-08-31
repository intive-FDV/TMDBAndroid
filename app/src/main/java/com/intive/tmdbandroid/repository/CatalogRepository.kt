package com.intive.tmdbandroid.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.intive.tmdbandroid.datasource.TVShowPagingSource
import com.intive.tmdbandroid.datasource.network.Service
import com.intive.tmdbandroid.entity.ResultTVShowsEntity
import com.intive.tmdbandroid.model.TVShow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatalogRepository @Inject constructor(
    private val service: Service,
) {
    companion object {
        const val DEFAULT_PAGE_SIZE = 20
    }

    fun paginatedPopularTVShows(): Flow<PagingData<TVShow>> {
        return Pager(
            config = PagingConfig(
                pageSize = DEFAULT_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                TVShowPagingSource(service = service)
            }
        ).flow
    }

    fun getTVShowByID(id:Int): Flow<TVShow>{
        return service.getTVShowByID(id)
    }
}