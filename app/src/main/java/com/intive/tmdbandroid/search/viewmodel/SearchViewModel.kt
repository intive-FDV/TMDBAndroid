package com.intive.tmdbandroid.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.intive.tmdbandroid.common.State
import com.intive.tmdbandroid.model.Screening
import com.intive.tmdbandroid.usecase.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<State<PagingData<Screening>>>(State.Waiting)

    val uiState: StateFlow<State<PagingData<Screening>>> = _state

    fun search(name:String) {
        viewModelScope.launch {
            searchUseCase(name)
                .onStart {
                    _state.value = State.Loading
                }
                .cachedIn(viewModelScope)
                .catch {
                    _state.value = State.Error
                }
                .collect {
                    _state.value = State.Success(it)
                }
        }
    }

}