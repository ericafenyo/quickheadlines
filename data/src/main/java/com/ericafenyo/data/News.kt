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

import com.ericafenyo.data.article.source.ArticleService
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class News {
  companion object {
    private const val NEW_YORK_TIMES_BASE_URL = "https://api.nytimes.com/svc/topstories/v2/"
  }

  private val gson: Gson = GsonBuilder()
    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
    .create()

  private fun getOkHttpClient(apiKeyPair: String) = OkHttpClient.Builder()
    .addInterceptor(ArticlePathInterceptor(apiKeyPair))
    .build()

  private fun getRetrofit(apiKey: String, baseUrl: String) = Retrofit.Builder()
    .baseUrl(baseUrl)
    .addConverterFactory(GsonConverterFactory.create(gson))
    .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
    .client(getOkHttpClient(apiKey))
    .build()

  fun getArticleService(apiKeyPair: String): ArticleService {
    return getRetrofit(apiKeyPair, NEW_YORK_TIMES_BASE_URL)
      .create(ArticleService::class.java)
  }
}
