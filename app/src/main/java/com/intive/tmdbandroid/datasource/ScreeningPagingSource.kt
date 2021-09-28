package com.intive.tmdbandroid.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.intive.tmdbandroid.datasource.network.Service
import com.intive.tmdbandroid.model.Screening
import kotlinx.coroutines.flow.collect

class ScreeningPagingSource(private val service: Service, private val type: Int) : PagingSource<Int, Screening>() {
    companion object {
        const val DEFAULT_PAGE_INDEX = 1

        const val TYPE_TVSHOW = 1
        const val TYPE_MOVIE = 2
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Screening> {
        return try {
            val pageNumber = params.key ?: DEFAULT_PAGE_INDEX

            lateinit var screenings: List<Screening>

            when (type) {
                TYPE_TVSHOW -> {
                    service.getPaginatedPopularTVShows(pageNumber).collect { screenings = it.toScreeningList() }
                }
                TYPE_MOVIE ->  {
                    service.getPaginatedPopularMovies(pageNumber).collect { screenings = it.toScreeningList() }
                }
                else -> throw RuntimeException("Ilegal type parameter")
            }


            val prevKey = if (pageNumber > DEFAULT_PAGE_INDEX) pageNumber - 1 else null
            val nextKey = if (screenings.isNotEmpty()) pageNumber + 1 else null

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