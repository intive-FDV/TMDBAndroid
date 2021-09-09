package com.intive.tmdbandroid.datasource

import android.util.Log
import androidx.paging.PagingSource
import com.intive.tmdbandroid.entity.ResultTVShowsEntity
import androidx.paging.PagingState
import com.intive.tmdbandroid.datasource.network.Service
import com.intive.tmdbandroid.model.TVShow
import kotlinx.coroutines.flow.collect
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

class TVShowSearchSource(private val service: Service, private val query: String) : PagingSource<Int, TVShow>() {
    companion object {
        const val DEFAULT_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TVShow> {
        return try {
            val pageNumber = params.key ?: DEFAULT_PAGE_INDEX
            Timber.d("pageNumber: $pageNumber")
            lateinit var response: ResultTVShowsEntity
            service.getTvShowByTitle(query, pageNumber).collect { response = it }

            val prevKey = if (pageNumber > DEFAULT_PAGE_INDEX) pageNumber - 1 else null
            val nextKey = if (response.TVShows.isNotEmpty()) pageNumber + 1 else null

            LoadResult.Page(
                data = response.toTVShowList(),
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, TVShow>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}