package com.intive.tmdbandroid.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.intive.tmdbandroid.common.state.Resource
import com.intive.tmdbandroid.entity.ResultMovies
import com.intive.tmdbandroid.model.TVShow
import com.intive.tmdbandroid.usecase.PaginatedPopularTVShowsUseCase
import com.intive.tmdbandroid.usecase.PopularMoviesUseCase
import com.intive.tmdbandroid.usecase.PopularTVShowsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject internal constructor(
    private val popularMoviesUseCase: PopularMoviesUseCase,
    private val popularTVShowsUseCase: PopularTVShowsUseCase,
    private val paginatedPopularTVShowsUseCase: PaginatedPopularTVShowsUseCase
) : ViewModel() {

    private val _popularMoviesFlow = Channel<Resource<ResultMovies>>(Channel.BUFFERED)
    val popularMoviesFlow = _popularMoviesFlow.receiveAsFlow()

    private val _popularTVShowsFlow = Channel<Resource<PagingData<TVShow>>>(Channel.BUFFERED)
    val popularTVShowsFlow = _popularTVShowsFlow.receiveAsFlow()

    fun popularMovies() {
        viewModelScope.launch {
            _popularMoviesFlow.send(Resource.loading())

            popularMoviesUseCase()
                .catch { e ->
                    _popularMoviesFlow.send(Resource.error(e.toString()))
                }
                .collect { resultMovies ->
                    _popularMoviesFlow.send(Resource.success(resultMovies))
                }
        }
    }

    fun popularTVShows() {
        viewModelScope.launch {
            _popularTVShowsFlow.send(Resource.loading())

            paginatedPopularTVShowsUseCase()
                .catch { e ->
                    Log.i("MAS", "tvshow usecase - catch $e")
                    _popularTVShowsFlow.send(Resource.error(e.toString()))
                }
                .collect { resultTVShows ->
                    Log.i("MAS", "tvshow usecase - collect $resultTVShows")
                    _popularTVShowsFlow.send(Resource.success(resultTVShows))
                }
        }
    }
}
