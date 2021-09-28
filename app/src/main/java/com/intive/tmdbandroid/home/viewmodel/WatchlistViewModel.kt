package com.intive.tmdbandroid.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.intive.tmdbandroid.common.State
import com.intive.tmdbandroid.model.Screening
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
class WatchlistViewModel @Inject internal constructor(
    private val getAllItemsInWatchlistUseCase: GetAllItemsInWatchlistUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<State<List<Screening>>>(State.Loading)
    val uiState: StateFlow<State<List<Screening>>> = _state

    fun watchlistScreening() {
        viewModelScope.launch {
            getAllItemsInWatchlistUseCase()
                .catch {
                    _state.value = State.Error
                }
                .collect {
                    _state.value = State.Success(it)
                }
        }
    }
}
