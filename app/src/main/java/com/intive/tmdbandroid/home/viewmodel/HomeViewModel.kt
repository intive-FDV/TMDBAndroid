package com.intive.tmdbandroid.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.intive.tmdbandroid.model.TVShow
import com.intive.tmdbandroid.usecase.PaginatedPopularTVShowsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject internal constructor(
    private val paginatedPopularTVShowsUseCase: PaginatedPopularTVShowsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Loading)

    val uiState: StateFlow<State> = _state

    fun popularTVShows() {
        viewModelScope.launch {
            paginatedPopularTVShowsUseCase()
                .catch { e ->
                    _state.value = State.Error(e)
                }
                .collect { resultTVShows ->
                    if (_state.value !is State.Error)
                        _state.value = State.Success(resultTVShows)
                }
        }
    }
}

sealed class State {
    object Loading : State()
    data class Success(val data: PagingData<TVShow>) : State()
    data class Error(val exception: Throwable) : State()
}
