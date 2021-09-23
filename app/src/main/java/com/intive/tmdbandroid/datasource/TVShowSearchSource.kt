package com.intive.tmdbandroid.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.intive.tmdbandroid.datasource.network.Service
import com.intive.tmdbandroid.entity.ResultListTVShowOrMovies
import com.intive.tmdbandroid.model.Screening
import kotlinx.coroutines.flow.collect

class TVShowSearchSource(private val service: Service, private val query: String) : PagingSource<Int, Screening>() {
    companion object {
        const val DEFAULT_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Screening> {
        return try {
            val pageNumber = params.key ?: DEFAULT_PAGE_INDEX
            lateinit var response: ResultListTVShowOrMovies
            service.getTvShowOrMovieByTitle(query, pageNumber).collect { response = it }

            val prevKey = if (pageNumber > DEFAULT_PAGE_INDEX) pageNumber - 1 else null
            val nextKey = if (response.results.isNotEmpty()) pageNumber + 1 else null

            val screenings: List<Screening> = response.results.map { it.toScreening() }

            LoadResult.Page(
                data = screenings,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Screening>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}