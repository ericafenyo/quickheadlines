@file:JvmName("Result")

package com.ericafenyo.data

/**
 * An enum class for handling a success and an error states.
 */
sealed class Result<out T : Any> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
}