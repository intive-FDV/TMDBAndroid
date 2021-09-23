package com.intive.tmdbandroid.repository

import com.intive.tmdbandroid.datasource.local.Dao
import com.intive.tmdbandroid.entity.MovieORMEntity
import com.intive.tmdbandroid.entity.TVShowORMEntity
import com.intive.tmdbandroid.model.Movie
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
    suspend fun delete(tvShowORMEntity: TVShowORMEntity) {
        return dao.deleteFavorite(tvShowORMEntity)
    }

    suspend fun checkIfExistAsFavorite(id: Int): Flow<Boolean>{
        return flowOf(dao.existAsFavorite(id))
    }

    // MOVIE METHODS

    suspend fun insertMovie(movieORMEntity: MovieORMEntity) {
        dao.insertMovie(movieORMEntity)
    }

    suspend fun deleteMovie(movieORMEntity: MovieORMEntity) {
        dao.deleteMovie(movieORMEntity)
    }

    suspend fun existMovie(id: Int): Flow<Boolean> {
        return flowOf(dao.existMovie(id))
    }

    suspend fun allMovies(): Flow<List<Movie>> {
        return flowOf(dao.allMovies().map { it.toMovie() })
    }
}