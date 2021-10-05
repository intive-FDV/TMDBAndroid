package com.intive.tmdbandroid.repository

import com.intive.tmdbandroid.datasource.local.Dao
import com.intive.tmdbandroid.entity.ScreeningORMEntity
import com.intive.tmdbandroid.model.Screening
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class WatchlistRepository @Inject constructor(
    private val dao: Dao
) {
    suspend fun allFavorites(): List<ScreeningORMEntity> {
        return dao.allFavorites()
    }

    suspend fun insert(screening: Screening) {
        dao.insertFavorite(screening.toScreeningORMEntity())
    }

    suspend fun delete(screening: Screening) {
        dao.deleteFavorite(screening.toScreeningORMEntity())
    }

    suspend fun exist(id: Int): Boolean {
        return dao.existAsFavorite(id)
    }
}