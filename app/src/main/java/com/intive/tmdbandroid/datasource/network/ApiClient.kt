package com.intive.tmdbandroid.datasource.network

import com.intive.tmdbandroid.entity.ResultTVShows
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiClient {
    @GET("tv/popular")
    suspend fun getPopularTVShows(@Query("api_key") apiKey: String) : Response<ResultTVShows>
}