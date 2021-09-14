package com.intive.tmdbandroid.details.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.intive.tmdbandroid.common.State
import com.intive.tmdbandroid.entity.TVShowORMEntity
import com.intive.tmdbandroid.model.TVShow
import com.intive.tmdbandroid.usecase.AddToWatchlistUseCase
import com.intive.tmdbandroid.usecase.DeleteFromWatchlistUseCase
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
    private val addToWatchlistUseCase: AddToWatchlistUseCase,
    private val deleteFromWatchlistUseCase: DeleteFromWatchlistUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<State<TVShow>>(State.Loading)
    val uiState: StateFlow<State<TVShow>> = _state

    private val _addToWatchlistState = MutableStateFlow<State<Boolean>>(State.Loading)
    val addToWatchlistUIState: StateFlow<State<Boolean>> = _addToWatchlistState

    private val _removeFromWatchlistState = MutableStateFlow<State<Boolean>>(State.Loading)
    val removeFromWatchlistUIState: StateFlow<State<Boolean>> = _removeFromWatchlistState

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

    fun addToWatchlist(id: String, tvShow: TVShowORMEntity) {
        viewModelScope.launch {
            addToWatchlistUseCase.addToWatchlist(id, tvShow)
                .catch {
                    _addToWatchlistState.value = State.Error
                }
                .collect {
                    _addToWatchlistState.value = State.Success(it)
                }
        }
    }

    fun deleteFromWatchlist(id: String) {
        viewModelScope.launch {
            deleteFromWatchlistUseCase.deleteFavorite(id)
                .catch {
                    _removeFromWatchlistState.value = State.Error
                }
                .collect {
                    _removeFromWatchlistState.value = State.Success(it)
                }
        }
    }
}