package com.intive.tmdbandroid.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.intive.tmdbandroid.datasource.ScreeningPagingSource
import com.intive.tmdbandroid.datasource.ScreeningSearchSource
import com.intive.tmdbandroid.datasource.network.Service
import com.intive.tmdbandroid.model.Movie
import com.intive.tmdbandroid.model.Screening
import com.intive.tmdbandroid.model.Session
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

    fun paginatedPopularTVShows(): Flow<PagingData<Screening>> {
        return Pager(
            config = PagingConfig(
                pageSize = DEFAULT_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                ScreeningPagingSource(service = service, 1)
            }
        ).flow
    }

    fun paginatedPopularMovies(): Flow<PagingData<Screening>> {
        return Pager(
            config = PagingConfig(
                pageSize = DEFAULT_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                ScreeningPagingSource(service = service, 2)
            }
        ).flow
    }

    fun getTVShowByID(id: Int): Flow<TVShow> {
        return service.getTVShowByID(id)
    }

    fun getMovieByID(id: Int): Flow<Movie> {
        return service.getMovieByID(id)
    }

    fun search(query: String, filterSelected: String?): Flow<PagingData<Screening>> {
        return Pager(
            config = PagingConfig(
                pageSize = DEFAULT_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                ScreeningSearchSource(service = service, query, filterSelected)
            }
        ).flow
    }

    fun getTVShowTrailer(id: Int): Flow<String> {
        return service.getTVShowVideos(id)
    }

    fun getMovieTrailer(id: Int): Flow<String> {
        return service.getMovieVideos(id)
    }

    suspend fun setMovieRating(idMovie: Int,rating: Double, session:Session):Boolean{
        return service.setMovieRating(idMovie,rating,session)
    }

    suspend fun setTVShowRating(idMovie: Int,rating: Double, session:Session):Boolean{
        return service.setTVShowRating(idMovie,rating,session)
    }

    fun getGuestSession():Flow<Session>{
        return service.getGuestSession()
    }

    fun getTVShowSimilar(id: Int): Flow<List<Screening>> {
        return service.getTvShowSimilar(id)
    }

    fun getMovieSimilar(id: Int): Flow<List<Screening>> {
        return service.getMovieSimilar(id)
    }
}
