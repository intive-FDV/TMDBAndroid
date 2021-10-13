package com.intive.tmdbandroid.datasource.network

import com.intive.tmdbandroid.entity.*
import com.intive.tmdbandroid.model.Movie
import com.intive.tmdbandroid.model.Session
import com.intive.tmdbandroid.model.TVShow
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiClient {
    // HOME
    @GET("tv/popular")
    suspend fun getPaginatedPopularTVShows(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): ResultTVShowsEntity

    @GET("movie/popular")
    suspend fun getPaginatedPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): ResultMoviesEntity

    // DETAIL

    @GET("tv/{tv_id}")
    suspend fun getTVShowByID(
        @Path("tv_id") tvShowID: Int,
        @Query("api_key") apiKey: String
    ): TVShow

    @GET("movie/{movie_id}")
    suspend fun getMovieByID(
        @Path("movie_id") movieID: Int,
        @Query("api_key") apiKey: String
    ): Movie

    @GET("tv/{tv_id}/videos")
    suspend fun getTVShowVideos(
        @Path("tv_id") tvShowID: Int,
        @Query("api_key") apiKey: String
    ): VideoEntity

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @Path("movie_id") movieID: Int,
        @Query("api_key") apiKey: String
    ): VideoEntity

    @POST("movie/{movie_id}/rating")
    suspend fun postMovieRanking(@Path("movie_id") query: Int, @Query("api_key") apiKey: String, @Query("guest_session_id") sessionId: String, @Body requestBody: RequestBody): Response<ResponseBody>

    @POST("tv/{tv_id}/rating")
    suspend fun postTVRanking(@Path("tv_id") query: Int, @Query("api_key") apiKey: String, @Query("guest_session_id") sessionId: String, @Body requestBody: RequestBody): Response<ResponseBody>

    @GET("authentication/guest_session/new")
    suspend fun getNewGuestSession(@Query("api_key") apiKey: String): Session


    @GET("person/{person_id}/combined_credits")
    suspend fun getCombinedCreditsByID(@Path("person_id") personID: Int,
                                       @Query("api_key") apiKey: String) : ResultCombinedCredits

    @GET("person/{person_id}")
    suspend fun getDetailPersonByID(@Path("person_id") personID: Int,
                                    @Query("api_key") apiKey: String) : ResultPerson

    @GET("tv/{tv_id}/similar")
    suspend fun getTVShowSimilar(
        @Path("tv_id") tvShowID: Int,
        @Query("api_key") apiKey: String
    ): ResultTVShowsEntity

    @GET("movie/{movie_id}/similar")
    suspend fun getMovieSimilar(
        @Path("movie_id") movieID: Int,
        @Query("api_key") apiKey: String
    ): ResultMoviesEntity

    // SEARCH

    @GET("search/multi")
    suspend fun getTVShowAndMoviesByName(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("page") page: Int
    ): ResultListTVShowOrMovies

    @GET("search/movie")
    suspend fun getMoviesByName(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("page") page: Int
    ): ResultMoviesEntity

    @GET("search/tv")
    suspend fun getTVByName(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("page") page: Int
    ): ResultTVShowsEntity

    @GET("search/person")
    suspend fun getPersonByName(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("page") page: Int
    ): ResultPeopleEntity
}