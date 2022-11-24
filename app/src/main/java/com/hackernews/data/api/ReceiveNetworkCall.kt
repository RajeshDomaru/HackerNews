package com.hackernews.data.api

import com.hackernews.util.UiText
import com.hackernews.util.extensions.validateString

fun <T> NetworkResponse<T, ErrorResponse>.receiveNetworkCall(): ApiResponse<T> {

    return when (this) {

        is NetworkResponse.ApiError -> {

            try {

                ApiResponse.Failure(code, UiText.DynamicString(body.message))

            } catch (e: Exception) {

                e.printStackTrace()

                ApiResponse.Failure(code)

            }

        }

        is NetworkResponse.NetworkError -> {

            try {

                ApiResponse.Failure(
                    hashCode(),
                    UiText.DynamicString(error.message.validateString())
                )

            } catch (e: Exception) {

                e.printStackTrace()

                ApiResponse.Failure(hashCode())

            }

        }

        is NetworkResponse.Success -> ApiResponse.Success(body)

        is NetworkResponse.UnknownError -> {

            try {

                ApiResponse.Failure(
                    error.hashCode(),
                    UiText.DynamicString(error?.message.validateString())
                )

            } catch (e: Exception) {

                e.printStackTrace()

                ApiResponse.Failure(hashCode())

            }

        }

        is NetworkResponse.EmptyResponse -> ApiResponse.EmptyData

    }

}
