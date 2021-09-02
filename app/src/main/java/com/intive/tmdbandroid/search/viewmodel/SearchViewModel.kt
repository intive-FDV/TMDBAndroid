package com.intive.tmdbandroid.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.intive.tmdbandroid.usecase.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Loading)

    val uiState: StateFlow<State> = _state

    fun search() {
        viewModelScope.launch {
            searchUseCase()
                .catch { e ->
                    _state.value = State.Error(e)
                }
                .collect {
                    _state.value = State.Success(it)
                }
        }
    }

}

sealed class State {
    object Loading : State()
    data class Success(val data: Any) : State()
    data class Error(val exception: Throwable) : State()
}