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


package com.ericafenyo.repository

import com.ericafenyo.data.news.ArticleDTO
import com.ericafenyo.data.news.LocalArticleDTO
import com.ericafenyo.entities.Article

fun toLocalArticle(value: LocalArticleDTO.Result) = with(value) {
  println(value)
  Article(
      imageUrl = urlToImage ?: "",
      articleUrl = url ?: "",
      author = author ?: "",
      description = description ?: "",
      publishedDate = publishedAt?: "",
      section = source.name,
      title = title ?: ""
  )
}

fun toArticle(value: ArticleDTO.Result) = with(value) {
  Article(
      imageUrl = multimedia.first().url,
      articleUrl = shortUrl,
      author = byline,
      description = abstract,
      publishedDate = publishedDate,
      section = section,
      title = title
  )
}



