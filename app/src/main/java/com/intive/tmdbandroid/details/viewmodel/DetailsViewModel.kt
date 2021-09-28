package com.intive.tmdbandroid.details.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.intive.tmdbandroid.common.State
import com.intive.tmdbandroid.datasource.network.Service
import com.intive.tmdbandroid.model.Screening
import com.intive.tmdbandroid.repository.CatalogRepository
import com.intive.tmdbandroid.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject internal constructor(
    private val tVShowUseCase: DetailTVShowUseCase,
    private val movieUseCase: DetailMovieUseCase,
    private val insertInWatchlistUseCase: InsertInWatchlistUseCase,
    private val deleteFromWatchlistUseCase: DeleteFromWatchlistUseCase,
    private val existUseCase: ExistUseCase,
    private val tvShowTrailerUseCase: GetTVShowTrailer,
    private val movieTrailerUseCase: GetMovieTrailer
) : ViewModel() {

    private val _state = MutableStateFlow<State<Screening>>(State.Loading)
    val uiState: StateFlow<State<Screening>> = _state

    private val _watchlistState = MutableStateFlow<State<Boolean>>(State.Loading)
    val watchlistUIState: StateFlow<State<Boolean>> = _watchlistState

    private val _trailerState = MutableStateFlow<State<String>>(State.Loading)
    val trailerState: StateFlow<State<String>> = _trailerState

    fun tVShows(id: Int) {
        viewModelScope.launch {
            tVShowUseCase(id)
                .catch {
                    _state.value = State.Error
                }
                .collect { tvShow ->
                    _state.value = State.Success(tvShow.toScreening())
                }
        }
    }

    fun getTVShowTrailer(id: Int) {
        viewModelScope.launch {
            tvShowTrailerUseCase(id)
                .catch {
                    Timber.i("MAS - viewModel.getTVShowTrailer.error: ${it.message}")
                    _trailerState.value = State.Error
                }
                .collect { trailerKey ->
                    _trailerState.value = State.Success(trailerKey)
                }
        }
    }

    fun movie(id: Int) {
        viewModelScope.launch {
            movieUseCase(id)
                .catch {
                    _state.value = State.Error
                }
                .collect { movie ->
                    _state.value = State.Success(movie.toScreening())
                }
        }
    }

    fun getMovieTrailer(id: Int) {
        viewModelScope.launch {
            movieTrailerUseCase(id)
                .catch {
                    _trailerState.value = State.Error
                }
                .collect { trailerKey ->
                    _trailerState.value = State.Success(trailerKey)
                }
        }
    }

    fun addToWatchlist(screening: Screening) {
        viewModelScope.launch {
            insertInWatchlistUseCase(screening)
                .catch {
                    _watchlistState.value = State.Error
                }
                .collect {
                    _watchlistState.value = State.Success(it)
                }
        }
    }

    fun deleteFromWatchlist(screening: Screening) {
        viewModelScope.launch {
            deleteFromWatchlistUseCase(screening)
                .catch {
                    _watchlistState.value = State.Error
                }
                .collect {
                    _watchlistState.value = State.Success(it)
                }
        }
    }

    fun existAsFavorite(id: Int) {
        viewModelScope.launch {
            existUseCase(id)
                .catch {
                    _watchlistState.value = State.Error
                }
                .collect {
                    _watchlistState.value = State.Success(it)
                }
        }
    }
}