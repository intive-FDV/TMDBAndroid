package com.intive.tmdbandroid.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.intive.tmdbandroid.common.State
import com.intive.tmdbandroid.model.Screening
import com.intive.tmdbandroid.usecase.GetAllItemsInWatchlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject internal constructor(
    private val getAllItemsInWatchlistUseCase: GetAllItemsInWatchlistUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<State<List<Screening>>>(State.Waiting)
    val uiState: StateFlow<State<List<Screening>>> = _state

    fun watchlistScreening() {
        viewModelScope.launch {
            getAllItemsInWatchlistUseCase()
                .onStart {
                    _state.value = State.Loading
                }
                .catch {
                    _state.value = State.Error
                }
                .collect {
                    _state.value = State.Success(it)
                }
        }
    }
}
