@file:JvmName("Listing")

package com.ericafenyo.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

data class Listing<T>(
  var data: LiveData<T> = MutableLiveData<T>(),
  var networkState: LiveData<NetworkState> = MutableLiveData<NetworkState>()
)