package com.intive.tmdbandroid.datasource.network

import android.R.attr
import com.intive.tmdbandroid.BuildConfig
import com.intive.tmdbandroid.common.RetrofitHelper
import com.intive.tmdbandroid.entity.ResultListTVShowOrMovies
import com.intive.tmdbandroid.entity.ResultTVShowsEntity
import com.intive.tmdbandroid.model.Movie
import com.intive.tmdbandroid.model.TVShow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import android.R.attr.rating
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import androidx.lifecycle.LiveData
import com.intive.tmdbandroid.common.State
import com.intive.tmdbandroid.model.Session


class Service {
    private val retrofit = RetrofitHelper.getRetrofit()
    private val session: LiveData<State<Session>>? = null

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

    fun getTvShowOrMovieByTitle(tvShowTitle: String, page: Int): Flow<ResultListTVShowOrMovies> {
        return flow {
            emit(retrofit.create(ApiClient::class.java).getTVShowAndMoviesByName(BuildConfig.API_KEY, tvShowTitle, page))
        }
    }

    fun getMovieByID(movieID: Int): Flow<Movie> {
        return flow {
            emit(retrofit.create(ApiClient::class.java).getMovieByID(movieID, BuildConfig.API_KEY))
        }
    }

    suspend fun setMovieRating(movieID: Int, rating: Double): Boolean {
        var retorno:Boolean = true
        if(rating < 0.5 || rating > 10){
            retorno=false
        }
        else{
            // Create JSON using JSONObject
            val jsonObject = JSONObject()
            jsonObject.put("value", rating)

            // Convert JSONObject to String
            val jsonObjectString = jsonObject.toString()

            // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
            val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

            retrofit.create(ApiClient::class.java).postMovieRanking(movieID,BuildConfig.API_KEY,session(),requestBody)
        }

        return  retorno
    }

    suspend fun setTVShowRating(tvShowID: Int, rating: Double): Boolean {
        var retorno:Boolean = true
        if(rating < 0.5 || rating > 10){
            retorno=false
        }
        else{
            // Create JSON using JSONObject
            val jsonObject = JSONObject()
            jsonObject.put("value", rating)

            // Convert JSONObject to String
            val jsonObjectString = jsonObject.toString()

            // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
            val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

            retrofit.create(ApiClient::class.java).postTVRanking(tvShowID,BuildConfig.API_KEY,session(),requestBody)
        }

        return  retorno
    }

    fun session():String{
        return session.toString()
    }
}
