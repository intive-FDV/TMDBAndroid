package com.intive.tmdbandroid.details.ui.person.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.intive.tmdbandroid.common.State
import com.intive.tmdbandroid.entity.ResultPerson
import com.intive.tmdbandroid.model.Screening
import com.intive.tmdbandroid.usecase.person.GetCombinedCreditsUseCase
import com.intive.tmdbandroid.usecase.person.GetDetailPersonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailPersonViewModel @Inject constructor(
    private val getCombinedCreditsUseCase: GetCombinedCreditsUseCase,
    private val getDetailPersonUseCase: GetDetailPersonUseCase
): ViewModel() {

    private val _stateCombinedCredits = MutableStateFlow<State<List<Screening>>>(State.Waiting)
    val uiStateCombinedCredits = _stateCombinedCredits

    private val _stateDetailPerson = MutableStateFlow<State<ResultPerson>>(State.Waiting)
    val uiStateDetailPerson = _stateDetailPerson

    fun getCombinedCredits(personID: Int) {
        viewModelScope.launch {
            getCombinedCreditsUseCase.invoke(personID)
                .onStart {
                    _stateCombinedCredits.value = State.Loading
                }
                .catch {
                    _stateCombinedCredits.value = State.Error
                }
                .collect { screenings ->
                    _stateCombinedCredits.value = State.Success(screenings)
                }
        }
    }

    fun getDetailPerson(personID: Int) {
        viewModelScope.launch {
            getDetailPersonUseCase(personID)
                .onStart {
                    _stateDetailPerson.value = State.Loading
                }
                .catch {
                    _stateDetailPerson.value = State.Error
                }
                .collect {
                    _stateDetailPerson.value = State.Success(it)
                }
        }
    }
}