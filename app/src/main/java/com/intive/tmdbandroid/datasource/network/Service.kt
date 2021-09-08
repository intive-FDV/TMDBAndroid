package com.intive.tmdbandroid.datasource.network

import com.intive.tmdbandroid.BuildConfig
import com.intive.tmdbandroid.common.RetrofitHelper
import com.intive.tmdbandroid.entity.ResultTVShowsEntity
import com.intive.tmdbandroid.model.TVShow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class Service {
    private val retrofit = RetrofitHelper.getRetrofit()

    fun getPaginatedPopularTVShows(page: Int): Flow<ResultTVShowsEntity> {
        return flow {
            emit(retrofit.create(ApiClient::class.java).getPaginatedPopularTVShows(BuildConfig.API_KEY, page))
        }
    }

    fun getTVShowByID(tvShowID: Int): Flow<TVShow> {
        return flow {
            emit(retrofit.create(ApiClient::class.java).getTVShowByID(tvShowID, BuildConfig.API_KEY))
        }
    }

    fun getTvShowByTitle(tvShowTitle: String, page: Int): Flow<ResultTVShowsEntity> {
        return flow {
            emit(retrofit.create(ApiClient::class.java).getTVShowByName(BuildConfig.API_KEY, tvShowTitle, page))
        }
    }
}
