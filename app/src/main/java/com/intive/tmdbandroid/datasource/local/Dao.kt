package com.intive.tmdbandroid.datasource.local

import androidx.room.*
import androidx.room.Dao
import com.intive.tmdbandroid.model.TVShow
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao{
    @Query("SELECT * FROM TVShow")
    suspend fun allFavorite(): List<TVShow>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(tvShowLocal: TVShow)

    @Update
    suspend fun updateFavorite(tvShowLocal: TVShow)

    @Delete
    suspend fun deleteFavorite(tvShowLocal: TVShow)

    @Query("SELECT * FROM TVShow WHERE id=:id LIMIT 1")
    suspend fun existAsFavorite(id: String): List<TVShow>

}