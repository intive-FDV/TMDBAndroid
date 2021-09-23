package com.intive.tmdbandroid.usecase

import com.intive.tmdbandroid.entity.MovieORMEntity
import com.intive.tmdbandroid.repository.WatchlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class InsertMovieToWatchlistUseCase @Inject constructor( private val repository: WatchlistRepository) {

    suspend operator fun invoke(movieORMEntity: MovieORMEntity): Flow<Boolean> {
        repository.insertMovie(movieORMEntity)
        return flowOf(true)
    }
}