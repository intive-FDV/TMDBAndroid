package com.intive.tmdbandroid.repository

import com.intive.tmdbandroid.datasource.local.Dao
import com.intive.tmdbandroid.entity.TVShowORMEntity
import com.intive.tmdbandroid.model.TVShow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class WatchlistRepository @Inject constructor(
    private val dao: Dao
) {

    suspend fun insert (tVShowORMEntity: TVShowORMEntity){
        return dao.insertFavorite(tVShowORMEntity)
    }

    suspend fun getFullWatchlist(): Flow<List<TVShow>> {
        return flowOf(dao.allFavorite().map { it.toTVShow() })
    }
    suspend fun delete(movie: TVShowORMEntity) {
        return dao.deleteFavorite(movie)
    }

    suspend fun checkIfExistAsFavorite(id: Int): Flow<Boolean>{
        return flowOf(dao.existAsFavorite(id))
    }
}