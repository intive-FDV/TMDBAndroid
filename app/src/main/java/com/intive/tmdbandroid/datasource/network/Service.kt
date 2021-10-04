package com.intive.tmdbandroid.datasource.network

import com.intive.tmdbandroid.BuildConfig
import com.intive.tmdbandroid.common.RetrofitHelper
import com.intive.tmdbandroid.entity.ResultListTVShowOrMovies
import com.intive.tmdbandroid.entity.ResultMoviesEntity
import com.intive.tmdbandroid.entity.ResultPeopleEntity
import com.intive.tmdbandroid.entity.ResultTVShowsEntity
import com.intive.tmdbandroid.model.Movie
import com.intive.tmdbandroid.model.TVShow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class Service {
    private val retrofit = RetrofitHelper.getRetrofit()

    // HOME

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

    // DETAIL

    fun getTVShowByID(tvShowID: Int): Flow<TVShow> {
        return flow {
            emit(retrofit.create(ApiClient::class.java).getTVShowByID(tvShowID, BuildConfig.API_KEY))
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

    // SEARCH

    fun getTvShowOrMovieByTitle(screeningTitle: String, page: Int): Flow<ResultListTVShowOrMovies> {
        return flow {
            emit(retrofit.create(ApiClient::class.java).getTVShowAndMoviesByName(BuildConfig.API_KEY, screeningTitle, page))
        }
    }

    fun getMovieByTitle(movieTitle: String, page: Int): Flow<ResultMoviesEntity> {
        return flow {
            emit(retrofit.create(ApiClient::class.java).getMoviesByName(BuildConfig.API_KEY, movieTitle, page))
        }
    }

    fun getTVByTitle(tvTitle: String, page: Int): Flow<ResultTVShowsEntity> {
        return flow {
            emit(retrofit.create(ApiClient::class.java).getTVByName(BuildConfig.API_KEY, tvTitle, page))
        }
    }

    fun getPersonByTitle(personName: String, page: Int): Flow<ResultPeopleEntity> {
        return flow {
            emit(retrofit.create(ApiClient::class.java).getPersonByName(BuildConfig.API_KEY, personName, page))
        }
    }
}
