package com.intive.tmdbandroid.details.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.intive.tmdbandroid.common.State
import com.intive.tmdbandroid.model.Screening
import com.intive.tmdbandroid.model.Session
import com.intive.tmdbandroid.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject internal constructor(
    private val tVShowUseCase: DetailTVShowUseCase,
    private val movieUseCase: DetailMovieUseCase,
    private val insertInWatchlistUseCase: InsertInWatchlistUseCase,
    private val existUseCase: ExistUseCase,
    private val tvShowTrailerUseCase: GetTVShowTrailerUseCase,
    private val getTVShowSimilarUseCase: GetTVShowSimilarUseCase,
    private val getMovieSimilarUseCase: GetMovieSimilarUseCase,
    private val movieTrailerUseCase: GetMovieTrailerUseCase,
    private val ratingMovieUseCase: RatingMovieUseCase,
    private val ratingTVShowUseCase: RatingTVShowUseCase,
    private val guestSessionUseCase: GuestSessionUseCase,
    private val sessionExistUseCase: SessionExistUseCase,
    private val insertInSessionUseCase: InsertInSessionUseCase,
    private val updateInWatchlistUseCase : UpdateFromWatchlistUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<State<Screening>>(State.Waiting)
    val uiState: StateFlow<State<Screening>> = _state

    private val _watchlistState = MutableStateFlow<State<Screening>>(State.Loading)
    val watchlistUIState: StateFlow<State<Screening>> = _watchlistState

    private lateinit var session:Session

    private val _trailerState = Channel<State<String>>()
    val trailerState = _trailerState

    private val _recommendedState = MutableStateFlow<State<List<Screening>>>(State.Waiting)
    val recommendedUIState: StateFlow<State<List<Screening>>> = _recommendedState

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
                .onStart {
                    _trailerState.send(State.Loading)
                }
                .catch {
                    _trailerState.send(State.Error)
                }
                .collect { trailerKey ->
                    _trailerState.send(State.Success(trailerKey))
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
                .onStart {
                    _trailerState.send(State.Loading)
                }
                .catch {
                    _trailerState.send(State.Error)
                }
                .collect { trailerKey ->
                    _trailerState.send(State.Success(trailerKey))
                }
        }
    }

    fun addToWatchlist(screening: Screening) {
        viewModelScope.launch {
            insertInWatchlistUseCase(screening)
                .onStart {
                    _watchlistState.value = State.Loading
                }
                .catch {
                    _watchlistState.value = State.Error
                }
                .collect {
                    _watchlistState.value = State.Success(it)
                }
        }
    }

    fun updateToWatchlist(screening: Screening) {
        viewModelScope.launch {
            updateInWatchlistUseCase(screening)
                .onStart {
                    _watchlistState.value = State.Loading
                }
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
                .onStart {
                    _watchlistState.value = State.Loading
                }
                .catch {
                    _watchlistState.value = State.Error
                }
                .collect {
                    _watchlistState.value = State.Success(it)
                }
        }
    }

    fun getTVShowSimilar(id: Int) {
        viewModelScope.launch {
            getTVShowSimilarUseCase(id)
                .onStart { _recommendedState.value = State.Loading }
                .catch { _recommendedState.value = State.Error }
                .collect { _recommendedState.value = State.Success(it) }
        }
    }

    fun getMovieSimilar(id: Int) {
        viewModelScope.launch {
            getMovieSimilarUseCase(id)
                .onStart { _recommendedState.value = State.Loading }
                .catch { _recommendedState.value = State.Error }
                .collect { _recommendedState.value = State.Success(it) }
        }
    }

    private suspend fun getSession() {
        session = sessionExistUseCase()
        if(session.id==0){
            val sessionFlow = guestSessionUseCase()
            session = sessionFlow.single()
            session.status_message="empty"
            insertInSessionUseCase(session)
        }


    }

    fun ratingMovie(idMovie: Int, rating: Double) {
        viewModelScope.launch {
            getSession()
            ratingMovieUseCase(idMovie, rating,session)
        }
    }

    fun ratingTvShow(idTVShow: Int, rating: Double) {
        viewModelScope.launch {
            getSession()
            ratingTVShowUseCase(idTVShow, rating,session)
        }
    }
}