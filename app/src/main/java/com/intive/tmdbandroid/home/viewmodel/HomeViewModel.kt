package com.intive.tmdbandroid.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.intive.tmdbandroid.common.State
import com.intive.tmdbandroid.usecase.PaginatedPopularTVShowsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject internal constructor(
    private val paginatedPopularTVShowsUseCase: PaginatedPopularTVShowsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Loading)
    val uiState: StateFlow<State> = _state

    fun popularTVShows() {
        if (_state.value !is State.Success<*>)
            viewModelScope.launch {
                paginatedPopularTVShowsUseCase()
                    .cachedIn(viewModelScope)
                    .catch {
                        _state.value = State.Error
                    }
                    .collect { resultTVShows ->
                        _state.value = State.Success(resultTVShows)
                    }
            }
    }

    fun watchlistTVShows() {
        //TODO implement after enable the Use Case for Room
    }
}
