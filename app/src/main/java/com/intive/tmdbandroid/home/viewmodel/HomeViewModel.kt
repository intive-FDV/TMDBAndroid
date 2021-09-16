package com.intive.tmdbandroid.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.intive.tmdbandroid.common.State
import com.intive.tmdbandroid.entity.TVShowORMEntity
import com.intive.tmdbandroid.model.TVShow
import com.intive.tmdbandroid.usecase.GetAllFromWatchlistUseCase
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
    private val getAllFromWatchlistUseCase: GetAllFromWatchlistUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<State<PagingData<TVShow>>>(State.Loading)
    val uiState: StateFlow<State<PagingData<TVShow>>> = _state

    private val _watchlistState = MutableStateFlow<State<List<TVShowORMEntity>>>(State.Loading)
    val watchlistUIState: StateFlow<State<List<TVShowORMEntity>>> = _watchlistState

    fun popularTVShows() {
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
        viewModelScope.launch {
            getAllFromWatchlistUseCase.allFavorites()
                .catch {
                    _watchlistState.value = State.Error
                }
                .collect {
                    _watchlistState.value = State.Success(it)
                }
        }
    }
}
