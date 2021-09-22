package com.intive.tmdbandroid.datasource.local

import androidx.room.*
import androidx.room.Dao
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

}