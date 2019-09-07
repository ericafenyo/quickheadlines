@file:JvmName("NetworkState")

package com.ericafenyo.data

enum class Status {
    LOADING,
    SUCCESS,
    ERROR
}

data class NetworkState(val status: Status, val exception: Exception? = null) {
    companion object {
        @JvmStatic
        val LOADING = NetworkState(Status.LOADING)
        @JvmStatic
        val SUCCESS = NetworkState(Status.SUCCESS)
        @JvmStatic
        val ERROR = NetworkState(Status.ERROR)
    }
}
