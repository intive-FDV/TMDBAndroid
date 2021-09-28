package com.intive.tmdbandroid.datasource.network

import com.intive.tmdbandroid.entity.ResultListTVShowOrMovies
import com.intive.tmdbandroid.entity.ResultTVShowsEntity
import com.intive.tmdbandroid.model.Movie
import com.intive.tmdbandroid.model.TVShow
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiClient {
    @GET("tv/popular")
    suspend fun getPaginatedPopularTVShows(@Query("api_key") apiKey: String,
                                           @Query("page") page: Int) : ResultTVShowsEntity

    @GET("tv/{tv_id}")
    suspend fun getTVShowByID(@Path("tv_id") tvShowID: Int,
                              @Query("api_key") apiKey: String) : TVShow

    @GET("movie/{movie_id}")
    suspend fun getMovieByID(@Path("movie_id") movieID: Int,
                              @Query("api_key") apiKey: String) : Movie

    @GET("search/multi")
    suspend fun getTVShowAndMoviesByName(@Query("api_key") apiKey: String,
                                @Query("query") query: String,
                                @Query("page") page: Int) : ResultListTVShowOrMovies

    @POST("movie/{movie_id}/rating")
    suspend fun postRanking(@Query("api_key") apiKey: String, @Path("movie_id") query: String, @Body requestBody : RequestBody): Response<ResponseBody>
}