package com.intive.tmdbandroid.datasource.network

import android.util.Log
import com.intive.tmdbandroid.BuildConfig
import com.intive.tmdbandroid.common.state.RetrofitHelper
import com.intive.tmdbandroid.entity.ResultTVShows
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.http.Query

class Service {
    private val retrofit = RetrofitHelper.getRertrofit()

    fun getPopularTVShows() : Flow<ResultTVShows> {
        return flow {
            emit(retrofit.create(ApiClient::class.java).getPopularTVShows(BuildConfig.API_KEY))
        }
    }
}