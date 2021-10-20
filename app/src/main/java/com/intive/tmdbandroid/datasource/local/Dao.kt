package com.intive.tmdbandroid.datasource.local

import androidx.room.*
import androidx.room.Dao
import com.intive.tmdbandroid.entity.room.ScreeningORMEntity
import com.intive.tmdbandroid.entity.SessionORMEntity

@Dao
interface Dao{
    @Query("SELECT * FROM ScreeningORMEntity")
    suspend fun allFavorites(): List<ScreeningORMEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(screeningORMEntity: ScreeningORMEntity)

    @Update
    suspend fun updateFavorite(screeningORMEntity: ScreeningORMEntity)

    @Delete
    suspend fun deleteFavorite(screeningORMEntity: ScreeningORMEntity)

    @Query("SELECT * FROM ScreeningORMEntity WHERE id = :id")
    suspend fun existAsFavorite(id: Int): ScreeningORMEntity

    @Query("SELECT * FROM SessionORMEntity WHERE id = 1")
    suspend fun existSession(): List<SessionORMEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(sessionORMEntity: SessionORMEntity)
}