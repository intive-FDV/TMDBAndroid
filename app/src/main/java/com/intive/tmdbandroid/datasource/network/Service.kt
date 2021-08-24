package com.intive.tmdbandroid.datasource.network

import android.util.Log
import com.intive.tmdbandroid.common.state.RetrofitHelper
import com.intive.tmdbandroid.entity.ResultTVShows
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.http.Query

class Service {
    private val retrofit = RetrofitHelper.getRertrofit()

    fun getPopularTVShows(apiKey: String) : Flow<ResultTVShows> {
        return flow {
            val response = retrofit.create(ApiClient::class.java).getPopularTVShows(apiKey)
            emit(response.body() ?: ResultTVShows(0, emptyList(), 0, 0))
        }
    }
}