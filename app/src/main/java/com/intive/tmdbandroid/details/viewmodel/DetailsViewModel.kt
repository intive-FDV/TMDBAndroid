package com.intive.tmdbandroid.details.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.intive.tmdbandroid.common.State
import com.intive.tmdbandroid.model.TVShow
import com.intive.tmdbandroid.usecase.DetailTVShowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject internal constructor(
    private val tVShowUseCase: DetailTVShowUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<State<TVShow>>(State.Loading)
    val uiState: StateFlow<State<TVShow>> = _state

    private val _addToWatchlistState = MutableStateFlow<State<Any>>(State.Loading)
    val addToWatchlistUIState: StateFlow<State<Any>> = _addToWatchlistState

    private val _removeFromWatchlistState = MutableStateFlow<State<Any>>(State.Loading)
    val removeFromWatchlistUIState: StateFlow<State<Any>> = _removeFromWatchlistState

    fun tVShows(id: Int) {
        viewModelScope.launch {
            tVShowUseCase(id)
                .catch {
                    _state.value = State.Error
                }
                .collect { tvShow ->
                    _state.value = State.Success(tvShow)
                }
        }
    }

    fun addToWatchlist() {
        //TODO implement to Use Case for Room
        viewModelScope.launch {
            _addToWatchlistState.value = State.Success<Any>("Add to watchlist")
        }
    }

    fun removeFromWatchlist() {
        //TODO implement to Use Case for Room
        viewModelScope.launch {
            _removeFromWatchlistState.value = State.Success<Any>("Remove from watchlist")
        }
    }
}