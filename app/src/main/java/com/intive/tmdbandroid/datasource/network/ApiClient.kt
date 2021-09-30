package com.intive.tmdbandroid.datasource.network

import com.intive.tmdbandroid.entity.ResultListTVShowOrMovies
import com.intive.tmdbandroid.entity.ResultMoviesEntity
import com.intive.tmdbandroid.entity.ResultTVShowsEntity
import com.intive.tmdbandroid.model.Movie
import com.intive.tmdbandroid.model.Session
import com.intive.tmdbandroid.model.TVShow
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiClient {
    @GET("tv/popular")
    suspend fun getPaginatedPopularTVShows(@Query("api_key") apiKey: String,
                                           @Query("page") page: Int) : ResultTVShowsEntity

    @GET("movie/popular")
    suspend fun getPaginatedPopularMovies(@Query("api_key") apiKey: String,
                                           @Query("page") page: Int) : ResultMoviesEntity

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
    suspend fun postMovieRanking(@Path("movie_id") query: Int,@Query("api_key") apiKey: String, @Query("guest_session_id") sessionId:String, @Body requestBody : RequestBody): Response<ResponseBody>

    @POST("tv/{tv_id}/rating")
    suspend fun postTVRanking(@Path("tv_id") query: Int,@Query("api_key") apiKey: String,  @Query("guest_session_id") sessionId:String,  @Body requestBody : RequestBody): Response<ResponseBody>

    @GET("authentication/guest_session/new")
    suspend fun getNewGuestSession(@Query("api_key") apiKey: String) : Session
}