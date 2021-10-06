package com.intive.tmdbandroid.repository

import com.intive.tmdbandroid.datasource.local.Dao
import com.intive.tmdbandroid.model.Screening
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class WatchlistRepository @Inject constructor(
    private val dao: Dao
) {
    suspend fun allFavorites(): Flow<List<Screening>> {
        return flowOf(
            dao.allFavorites().map {
                it.toScreening()
            }
        )
    }

    suspend fun insert(screening: Screening) : Flow<Boolean> {
        dao.insertFavorite(screening.toScreeningORMEntity())
        return flowOf(true)
    }

    suspend fun delete(screening: Screening) : Flow<Boolean> {
        dao.deleteFavorite(screening.toScreeningORMEntity())
        return flowOf(false)
    }

    suspend fun exist(id: Int): Flow<Boolean> {
        return flowOf(dao.existAsFavorite(id))
    }
}