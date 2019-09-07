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

package com.ericafenyo.data.article.source

import androidx.lifecycle.MutableLiveData
import com.ericafenyo.data.Mapper
import com.ericafenyo.data.NetworkState
import com.ericafenyo.data.Result
import com.ericafenyo.data.entities.Article
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException

/**
 *
 */
interface ArticleRemoteDataSource {
  /**
   * Returns an array of articles currently on the specified section.
   *
   * @param section  An enum class [Section] representing the section the story appears in.
   *
   * @return A a list of [Article] wrapped in a [Result].
   */

  suspend fun fetchTopStories(section: Section): Result<List<Article>>


  /**
   *
   */
  suspend fun search(query: String, filter: String)

}


/**
 * An implementation detail of [ArticleRemoteDataSource]
 *
 * @property service the api service used.
 */
class ArticleRemoteDataSourceImlp constructor(
  private val service: ArticleService,
  private val mapper: Mapper
) :
  ArticleRemoteDataSource {
  private val LOG_TAG = ArticleRemoteDataSource::class.java.name

  private val networkState = MutableLiveData<NetworkState>()

  override suspend fun fetchTopStories(section: Section): Result<List<Article>> {
    // Start with a loading state.
    networkState.postValue(NetworkState.LOADING)
    return try {

      val articles = service.fetchTopStories(section.name)
        .await()
        .articles
        .map(mapper::toArticle)
      Result.Success(articles)
    } catch (exception: Exception) {
      return when (exception) {
        // Error from server
        is HttpException -> return Result.Error("Server error")
        // Network errors
        is IOException -> Result.Error("Network connection error!")
        else -> Result.Error("Unknown error")
      }
    }
  }

  override suspend fun search(query: String, filter: String) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}


suspend fun <T> retryIO(
  times: Int = 4,
  initialDelay: Long = 100, // 0.1 second
  maxDelay: Long = 1000,    // 1 second
  factor: Double = 2.0,
  block: suspend () -> T
): T {
  var currentDelay = initialDelay
  repeat(times - 1) {

    try {
      return block()
    } catch (exception: IOException) {
      // you can log an error here and/or make a more finer-grained
      // analysis of the cause to see if retry is needed
    }
    delay(currentDelay)
    currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
  }
  return block() // last attempt
}

