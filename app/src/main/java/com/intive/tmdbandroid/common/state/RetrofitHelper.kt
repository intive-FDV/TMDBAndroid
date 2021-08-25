package com.intive.tmdbandroid.common.state

import com.intive.tmdbandroid.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient

import okhttp3.logging.HttpLoggingInterceptor

object RetrofitHelper {
    fun getRertrofit() : Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        // REMOVE COMMENT TO ENABLE LOGS
        //val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            //.client(client)                       //REMOVE COMMENT TO ENABLE LOGS
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}