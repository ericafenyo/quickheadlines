/*
 * Copyright (C) 2019 Eric Afenyo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:JvmName("PagedArticleDataSource")

package com.ericafenyo.data.article

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.ericafenyo.data.Mapper
import com.ericafenyo.data.NetworkState
import com.ericafenyo.data.article.source.ArticleService
import com.ericafenyo.data.article.source.Section
import com.ericafenyo.data.entities.Article
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PagedArticleDataSource constructor(
  private val service: ArticleService,
  private val mapper: Mapper
) :
  PageKeyedDataSource<Int, List<Article>>() {

  // keep a function reference for the retry event
  private var retry: (() -> Any)? = null

  // keep a reference of the network state.
  val networkState = MutableLiveData<NetworkState>()

  fun retryAllFailed() {
    val prevRetry = retry
    retry = null
    prevRetry?.let {
      GlobalScope.launch {
        it.invoke()
      }
    }
  }

  override fun loadInitial(
    params: LoadInitialParams<Int>,
    callback: LoadInitialCallback<Int, List<Article>>
  ) {
    val initialPage = 20

    runBlocking {
      // Start with a loading state
      networkState.postValue(NetworkState.LOADING)
      try {
        val article = service.fetchTopStories(Section.home.name).await()
          .articles
          .map(mapper::toArticle)

      } catch (exception: Exception) {
        // Prepare the retry function for invocation.
        retry = {
          loadInitial(params, callback)
        }
        // Notify Error
        networkState.postValue(NetworkState.ERROR)
      }
    }
  }

  override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, List<Article>>) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, List<Article>>) {
    // Not implemented
  }
}