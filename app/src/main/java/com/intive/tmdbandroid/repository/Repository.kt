package com.intive.tmdbandroid.repository

import android.util.Log
import com.intive.tmdbandroid.datasource.network.Service
import com.intive.tmdbandroid.entity.ResultTVShows
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val service: Service,
) {
    private val apiKey = "dc41c63c639111ed0845809d99175035"

    fun popularTVShows() : Flow<ResultTVShows> {
        val result = service.getPopularTVShows(apiKey)
        Log.i("MAS", "Repository result: $result")
        return result
    }
}