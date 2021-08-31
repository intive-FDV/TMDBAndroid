package com.intive.tmdbandroid.datasource.network

import com.intive.tmdbandroid.entity.ResultTVShowsEntity
import com.intive.tmdbandroid.model.TVShow
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiClient {
    @GET("tv/popular")
    suspend fun getPaginatedPopularTVShows(@Query("api_key") apiKey: String,
                                           @Query("page") page: Int) : ResultTVShowsEntity

    @GET("tv/{tv_id}")
    suspend fun getTVShowByID(@Path("tv_id") tvShowID: Int,
                              @Query("api_key") apiKey: String) : TVShow
}