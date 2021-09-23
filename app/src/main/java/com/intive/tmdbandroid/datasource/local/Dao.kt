package com.intive.tmdbandroid.datasource.local

import androidx.room.*
import androidx.room.Dao
import com.intive.tmdbandroid.entity.MovieORMEntity
import com.intive.tmdbandroid.entity.TVShowORMEntity

@Dao
interface Dao{
    @Query("SELECT * FROM TVShowORMEntity")
    suspend fun allFavorite(): List<TVShowORMEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(tvShowLocal: TVShowORMEntity)

    @Update
    suspend fun updateFavorite(tvShowLocal: TVShowORMEntity)

    @Delete
    suspend fun deleteFavorite(tvShowLocal: TVShowORMEntity)

    @Query("SELECT EXISTS(SELECT * FROM TVShowORMEntity WHERE id = :id)")
    suspend fun existAsFavorite(id: Int): Boolean

    // MOVIE METHODS

    @Query("SELECT * FROM MovieORMEntity")
    suspend fun allMovies(): List<MovieORMEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movieORMEntity: MovieORMEntity)

    @Delete
    suspend fun deleteMovie(movieORMEntity: MovieORMEntity)

    @Query("SELECT EXISTS(SELECT * FROM MovieORMEntity WHERE id = :id)")
    suspend fun existMovie(id: Int): Boolean
}