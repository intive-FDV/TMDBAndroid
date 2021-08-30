package com.intive.tmdbandroid.datasource.network

import com.intive.tmdbandroid.BuildConfig
import com.intive.tmdbandroid.common.state.RetrofitHelper
import com.intive.tmdbandroid.entity.ResultTVShows
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class Service {
    private val retrofit = RetrofitHelper.getRetrofit()

    fun getPopularTVShows() : Flow<ResultTVShows> {
        return flow {
            emit(retrofit.create(ApiClient::class.java).getPopularTVShows(BuildConfig.API_KEY))
        }
    }

    fun getPaginatedPopularTVShows(page: Int) : Flow<ResultTVShows> {
        return flow {
            emit(retrofit.create(ApiClient::class.java).getPaginatedPopularTVShows(BuildConfig.API_KEY, page))
        }
    }
}