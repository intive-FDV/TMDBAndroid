package com.intive.tmdbandroid.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.intive.tmdbandroid.common.State
import com.intive.tmdbandroid.model.TVShow
import com.intive.tmdbandroid.usecase.GetAllItemsInWatchlistUseCase
import com.intive.tmdbandroid.usecase.PaginatedPopularTVShowsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TVShowsViewModel @Inject internal constructor(
    private val paginatedPopularTVShowsUseCase: PaginatedPopularTVShowsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<State<PagingData<TVShow>>>(State.Loading)
    val uiState: StateFlow<State<PagingData<TVShow>>> = _state

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
}
