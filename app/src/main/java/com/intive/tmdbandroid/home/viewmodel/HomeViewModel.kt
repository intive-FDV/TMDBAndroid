package com.intive.tmdbandroid.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.intive.tmdbandroid.model.TVShow
import com.intive.tmdbandroid.usecase.PaginatedPopularTVShowsUseCase
import com.intive.tmdbandroid.usecase.PopularMoviesUseCase
import com.intive.tmdbandroid.usecase.PopularTVShowsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject internal constructor(
    private val popularMoviesUseCase: PopularMoviesUseCase,
    private val popularTVShowsUseCase: PopularTVShowsUseCase,
    private val paginatedPopularTVShowsUseCase: PaginatedPopularTVShowsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Loading)

    val uiState: StateFlow<State> = _state

    fun popularTVShows() {
        viewModelScope.launch {
            paginatedPopularTVShowsUseCase()
                .catch { e ->
                    Log.i("MAS", "tvshow usecase - catch $e")
                    _state.value = State.Error(e)
                }
                .collect { resultTVShows ->
                    Log.i("MAS", "tvshow usecase - collect $resultTVShows")
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
