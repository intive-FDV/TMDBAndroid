package com.intive.tmdbandroid.details.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.intive.tmdbandroid.common.State
import com.intive.tmdbandroid.entity.TVShowORMEntity
import com.intive.tmdbandroid.model.TVShow
import com.intive.tmdbandroid.usecase.DetailTVShowUseCase
import com.intive.tmdbandroid.usecase.GetIfExistsUseCase
import com.intive.tmdbandroid.usecase.RemoveTVShowFromWatchlistUseCase
import com.intive.tmdbandroid.usecase.SaveTVShowInWatchlistUseCase
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
    private val saveTVShowInWatchlistUseCase: SaveTVShowInWatchlistUseCase,
    private val removeTVShowFromWatchlistUseCase: RemoveTVShowFromWatchlistUseCase,
    private val getIfExistsUseCase: GetIfExistsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<State<TVShow>>(State.Loading)
    val uiState: StateFlow<State<TVShow>> = _state

    private val _watchlistState = MutableStateFlow<State<Boolean>>(State.Loading)
    val watchlistUIState: StateFlow<State<Boolean>> = _watchlistState

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

    fun addToWatchlist(tvShow: TVShowORMEntity) {
        viewModelScope.launch {
            saveTVShowInWatchlistUseCase(tvShow)
                .catch {
                    _watchlistState.value = State.Error
                }
                .collect {
                    _watchlistState.value = State.Success(it)
                }
        }
    }

    fun deleteFromWatchlist(tvShow: TVShowORMEntity) {
        viewModelScope.launch {
            removeTVShowFromWatchlistUseCase(tvShow)
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
            getIfExistsUseCase(id)
                .catch {
                    _watchlistState.value = State.Error
                }
                .collect {
                    _watchlistState.value = State.Success(it)
                }
        }
    }
}