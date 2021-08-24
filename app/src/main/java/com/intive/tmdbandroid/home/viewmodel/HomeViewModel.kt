package com.intive.tmdbandroid.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.intive.tmdbandroid.common.state.Resource
import com.intive.tmdbandroid.entity.ResultMovies
import com.intive.tmdbandroid.entity.ResultTVShows
//import com.intive.tmdbandroid.sample.model.Sample
import com.intive.tmdbandroid.sample.usecase.SampleUseCase
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
    private val sampleUseCase: SampleUseCase,
    private val popularMoviesUseCase: PopularMoviesUseCase,
    private val popularTVShowsUseCase: PopularTVShowsUseCase
) : ViewModel() {

//    private val _sampleFlow = Channel<Resource<Sample>>(Channel.BUFFERED)
//    val sampleFlow = _sampleFlow.receiveAsFlow()

    private val _popularMoviesFlow = Channel<Resource<ResultMovies>>(Channel.BUFFERED)
    val popularMoviesFlow = _popularMoviesFlow.receiveAsFlow()

    private val _popularTVShowsFlow = Channel<Resource<ResultTVShows>>(Channel.BUFFERED)
    val popularTVShowsFlow = _popularTVShowsFlow.receiveAsFlow()

    /*fun sample() {
        viewModelScope.launch {
            _sampleFlow.send(Resource.loading())

            sampleUseCase()
                .catch { e ->
                    _sampleFlow.send(Resource.error(e.toString()))
                }
                .collect { sample ->
                    _sampleFlow.send(Resource.success(sample))

                }
        }
    }*/

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

            popularTVShowsUseCase()
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
