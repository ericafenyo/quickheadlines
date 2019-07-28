@file:JvmName("HeadlineViewModel")

package com.ericafenyo.quickheadline.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.ericafenyo.data.entities.Article
import com.ericafenyo.quickheadline.ScopedViewModel
import com.ericafenyo.repository.NewsRepositoryImpl

class HeadlineViewModel : ScopedViewModel() {
    private val repository = NewsRepositoryImpl()

    val article: LiveData<List<Article>> = liveData {
        val data = repository.getTopStories("home")
        emit(data)
    }
}