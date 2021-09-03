package com.intive.tmdbandroid.common

sealed class State {
    object Loading : State()
    data class Success<T>(val data: T) : State()
    object Error : State()
}
