package com.intive.tmdbandroid.common

sealed class State<out T> {
    object Loading : State<Nothing>()
    data class Success<T>(val data: T) : State<T>()
    object Error : State<Nothing>()
}
