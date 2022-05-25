package com.bridge.androidtechnicaltest.model

sealed class Resource<T> {
    data class Success<T: Any>(val data: T): Resource<T>()
    data class Error<T: Any>(val exception: Throwable?): Resource<T>()
    class Loading<T : Any> : Resource<T>()
}

enum class Status {
    LOADING, SUCCESS, ERROR
}