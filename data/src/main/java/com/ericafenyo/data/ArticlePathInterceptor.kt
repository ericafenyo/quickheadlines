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

package com.ericafenyo.data

import com.ericafenyo.data.ArticlePathInterceptor.Companion.TOKEN_KEY
import okhttp3.Interceptor
import okhttp3.Response

/**
 * An [Interceptor] for appending an api key-value pair to a url.
 *
 * @property tokenValue the value for the query parameter representing
 * the web service API KEY.
 * @property TOKEN_KEY the `key` for the query parameter.
 * */
class ArticlePathInterceptor(private val tokenValue: String) : Interceptor {

  companion object {
    private const val TOKEN_KEY = "api-key"
  }

  override fun intercept(chain: Interceptor.Chain): Response {

    val request = chain.request()
    val url = request.url().newBuilder().addQueryParameter(TOKEN_KEY, tokenValue).build()
    val newRequest = request.newBuilder().url(url).build()
    // TODO: delete the print statement
    println(newRequest.url())
    return chain.proceed(newRequest)
  }
}