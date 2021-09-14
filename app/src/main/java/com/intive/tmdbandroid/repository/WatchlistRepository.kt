package com.intive.tmdbandroid.repository

import com.intive.tmdbandroid.datasource.local.Dao
import com.intive.tmdbandroid.entity.TVShowORMEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WatchlistRepository @Inject constructor(
    private val dao: Dao
) {

    suspend fun allFavorites(): List<TVShowORMEntity> {
        return dao.allFavorite()
    }

    suspend fun insertFavorite(tvShow: TVShowORMEntity) {
        dao.insertFavorite(tvShow)
    }

    suspend fun deleteFavorite(tvShow: TVShowORMEntity) {
        dao.deleteFavorite(tvShow)
    }

    suspend fun existAsFavorite(id: String): List<TVShowORMEntity> {
        return dao.existAsFavorite(id)
    }

}