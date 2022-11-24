package com.hackernews.data.api

import java.io.IOException

sealed class NetworkResponse<out T, out U> {

    // Success response with body
    data class Success<T>(val body: T) : NetworkResponse<T, Nothing>()

    // Failure response with body
    data class ApiError<U>(val body: U, val code: Int) : NetworkResponse<Nothing, U>()

    // Network error
    data class NetworkError(val error: IOException) : NetworkResponse<Nothing, Nothing>()

    // For example, json parsing error
    data class UnknownError(val error: Throwable?) : NetworkResponse<Nothing, Nothing>()

    object EmptyResponse : NetworkResponse<Nothing, Nothing>()

}