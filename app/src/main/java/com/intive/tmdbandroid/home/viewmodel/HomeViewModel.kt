package com.intive.tmdbandroid.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.intive.tmdbandroid.common.state.Resource
import com.intive.tmdbandroid.sample.domain.Sample
import com.intive.tmdbandroid.sample.usecase.SampleUseCase
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
) : ViewModel() {

    private val _sampleFlow = Channel<Resource<Sample>>(Channel.BUFFERED)
    val sampleFlow = _sampleFlow.receiveAsFlow()

    fun sample() {
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
    }
}
