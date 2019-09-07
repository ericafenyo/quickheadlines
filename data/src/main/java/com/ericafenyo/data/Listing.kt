@file:JvmName("Listing")

package com.ericafenyo.data

import androidx.lifecycle.LiveData

data class Listing<T>(val data: LiveData<T>, val networkState: LiveData<NetworkState>)