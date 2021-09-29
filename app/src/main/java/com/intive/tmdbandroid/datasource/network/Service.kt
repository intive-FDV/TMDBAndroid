package com.intive.tmdbandroid.datasource.network

import com.intive.tmdbandroid.BuildConfig
import com.intive.tmdbandroid.common.RetrofitHelper
import com.intive.tmdbandroid.entity.ResultListTVShowOrMovies
import com.intive.tmdbandroid.entity.ResultMoviesEntity
import com.intive.tmdbandroid.entity.ResultTVShowsEntity
import com.intive.tmdbandroid.model.Movie
import com.intive.tmdbandroid.model.TVShow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class Service {
    private val retrofit = RetrofitHelper.getRetrofit()

    fun getPaginatedPopularTVShows(page: Int): Flow<ResultTVShowsEntity> {
        return flow {
            emit(retrofit.create(ApiClient::class.java).getPaginatedPopularTVShows(BuildConfig.API_KEY, page))
        }
    }

    fun getPaginatedPopularMovies(page: Int): Flow<ResultMoviesEntity> {
        return flow {
            emit(retrofit.create(ApiClient::class.java).getPaginatedPopularMovies(BuildConfig.API_KEY, page))
        }
    }

    fun getTVShowByID(tvShowID: Int): Flow<TVShow> {
        return flow {
            emit(retrofit.create(ApiClient::class.java).getTVShowByID(tvShowID, BuildConfig.API_KEY))
        }
    }

    fun getTvShowOrMovieByTitle(tvShowTitle: String, page: Int): Flow<ResultListTVShowOrMovies> {
        return flow {
            emit(retrofit.create(ApiClient::class.java).getTVShowAndMoviesByName(BuildConfig.API_KEY, tvShowTitle, page))
        }
    }

    fun getMovieByID(movieID: Int): Flow<Movie> {
        return flow {
            emit(retrofit.create(ApiClient::class.java).getMovieByID(movieID, BuildConfig.API_KEY))
        }
    }

    fun getTVShowVideos(tvShowID: Int): Flow<String> {
        return flow {
            val videoList = retrofit.create(ApiClient::class.java).getTVShowVideos(tvShowID, BuildConfig.API_KEY).results

            val official = videoList.filter { it.site == "YouTube" && it.type == "Trailer" && it.official }
            val unOfficial = videoList.filter { it.site == "YouTube" && it.type == "Trailer" }

            when {
                official.isNotEmpty() -> emit(official[0].key)
                unOfficial.isNotEmpty() -> emit(unOfficial[0].key)
                else -> emit("")
            }
        }
    }

    fun getMovieVideos(movieID: Int): Flow<String> {
        return flow {
            val videoList = retrofit.create(ApiClient::class.java).getMovieVideos(movieID, BuildConfig.API_KEY).results

            val official = videoList.filter { it.site == "YouTube" && it.type == "Trailer" && it.official }
            val unOfficial = videoList.filter { it.site == "YouTube" && it.type == "Trailer" }

            when {
                official.isNotEmpty() -> emit(official[0].key)
                unOfficial.isNotEmpty() -> emit(unOfficial[0].key)
                else -> emit("")
            }
        }
    }
}
