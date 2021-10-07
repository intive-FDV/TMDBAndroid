package com.intive.tmdbandroid.repository

import com.intive.tmdbandroid.datasource.local.Dao
import com.intive.tmdbandroid.model.Screening
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import javax.inject.Inject

class WatchlistRepository @Inject constructor(
    private val dao: Dao
) {
    suspend fun allFavorites(): Flow<List<Screening>> {
        return flowOf(
            dao.allFavorites().filter { it.my_favorite }.map {
                it.toScreening()
            }
        )
    }

    suspend fun insert(screening: Screening)  = dao.insertFavorite(screening.toScreeningORMEntity())

    suspend fun delete(screening: Screening) {
        dao.deleteFavorite(screening.toScreeningORMEntity())
    }

    suspend fun updateFavorite(screening: Screening) {
        dao.updateFavorite(screening.toScreeningORMEntity())
    }

    suspend fun exist(id: Int): Flow<Screening> {
        val existFlow = dao.existAsFavorite(id)?.toScreening()
        return flowOf(existFlow)
    }
}