package com.example.dunzoassignment.network

import java.lang.Exception

sealed class NetworkResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : NetworkResult<T>()
    data class Error(val exception: Throwable) : NetworkResult<Nothing>()
    data class NetworkError(val exception: Throwable = Exception()) : NetworkResult<Nothing>()
}