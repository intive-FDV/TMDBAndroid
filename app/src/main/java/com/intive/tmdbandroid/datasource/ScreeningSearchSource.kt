package com.intive.tmdbandroid.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.intive.tmdbandroid.datasource.network.Service
import com.intive.tmdbandroid.model.Screening
import kotlinx.coroutines.flow.collect

class ScreeningSearchSource(
    private val service: Service,
    private val query: String,
    private val filterSelected: String?
) : PagingSource<Int, Screening>() {
    companion object {
        const val DEFAULT_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Screening> {
        return try {
            val pageNumber = params.key ?: DEFAULT_PAGE_INDEX

            return when(filterSelected) {
                "Movies" -> {
                    var screenings: List<Screening> = emptyList()
                    var prevKey: Int? = null
                    var nextKey: Int? = null
                    service.getMovieByTitle(query, pageNumber).collect { response ->
                        prevKey = if (pageNumber > DEFAULT_PAGE_INDEX) pageNumber - 1 else null
                        nextKey = if (response.list.isNotEmpty()) pageNumber + 1 else null
                        screenings = response.list.map { it.toScreening() }
                    }
                    LoadResult.Page(
                        data = screenings,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
                "TV Shows" -> {
                    var screenings: List<Screening> = emptyList()
                    var prevKey: Int? = null
                    var nextKey: Int? = null
                    service.getTVByTitle(query, pageNumber).collect { response ->
                        prevKey = if (pageNumber > DEFAULT_PAGE_INDEX) pageNumber - 1 else null
                        nextKey = if (response.list.isNotEmpty()) pageNumber + 1 else null
                        screenings = response.list.map { it.toScreening() }
                    }
                    LoadResult.Page(
                        data = screenings,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
                "All" -> {
                    var screenings: List<Screening> = emptyList()
                    var prevKey: Int? = null
                    var nextKey: Int? = null
                    service.getTvShowOrMovieByTitle(query, pageNumber).collect { response ->
                        prevKey = if (pageNumber > DEFAULT_PAGE_INDEX) pageNumber - 1 else null
                        nextKey = if (response.results.isNotEmpty()) pageNumber + 1 else null
                        screenings = response.results.map { it.toScreening() }
                    }
                    LoadResult.Page(
                        data = screenings,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
                "People" -> {
                    var screenings: List<Screening> = emptyList()
                    var prevKey: Int? = null
                    var nextKey: Int? = null
                    service.getPersonByTitle(query, pageNumber).collect { response ->
                        prevKey = if (pageNumber > DEFAULT_PAGE_INDEX) pageNumber - 1 else null
                        nextKey = if (response.results.isNotEmpty()) pageNumber + 1 else null
                        screenings = response.results.map { it.toScreening() }
                    }
                    LoadResult.Page(
                        data = screenings,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
                else -> LoadResult.Error(Exception("No filter available"))
            }
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