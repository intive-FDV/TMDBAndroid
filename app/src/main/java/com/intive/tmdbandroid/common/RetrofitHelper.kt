package com.intive.tmdbandroid.common

import com.intive.tmdbandroid.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Duration

object RetrofitHelper {
    fun getRetrofit() : Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .callTimeout(Duration.ofMinutes(1))
            .connectTimeout(Duration.ofSeconds(30))
            .readTimeout(Duration.ofSeconds(30))
            .writeTimeout(Duration.ofSeconds(30))
            .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}