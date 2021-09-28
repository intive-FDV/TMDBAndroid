package com.intive.tmdbandroid.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.intive.tmdbandroid.common.State
import com.intive.tmdbandroid.model.Screening
import com.intive.tmdbandroid.usecase.PaginatedPopularMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject internal constructor(
    private val paginatedPopularMoviesUseCase: PaginatedPopularMoviesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<State<PagingData<Screening>>>(State.Loading)
    val uiState: StateFlow<State<PagingData<Screening>>> = _state
    
    fun popularMovies() {
        viewModelScope.launch {
            paginatedPopularMoviesUseCase()
                .cachedIn(viewModelScope)
                .catch {
                    _state.value = State.Error
                }
                .collect { resultMovies ->
                    _state.value = State.Success(resultMovies)
                }
        }
    }
}
