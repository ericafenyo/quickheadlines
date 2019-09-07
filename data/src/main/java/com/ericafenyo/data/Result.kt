@file:JvmName("Result")

package com.ericafenyo.data.article

/**
 * An enum class for handling a success and an error states.
 */
sealed class Result<out T : Any> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}