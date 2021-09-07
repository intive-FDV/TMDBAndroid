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
        //TODO implement after enable the Use Case for Room
    }

    fun removeFromWatchlist() {
        //TODO implement after enable the Use Case for Room
    }
}