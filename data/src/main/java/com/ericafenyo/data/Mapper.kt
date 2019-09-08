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

import com.ericafenyo.data.article.source.ArticleDTO
import com.ericafenyo.data.entities.Article

class Mapper {
  fun toArticle(value: ArticleDTO.Result) = with(value) {
    println(value)
    Article(
      imageUrl = parseMedia(multimedia),
      articleUrl = url,
      author = byline ?: "",
      description = abstract ?: url,
      publishedDate = publishedDate ?: "",
      section = section ?: "",
      title = title ?: ""
    )
  }

  private fun parseMedia(multimedia: List<ArticleDTO.Result.Multimedia>?): String {
    if (multimedia.isNullOrEmpty()) {
      return ""
    }

    return multimedia.last().url
  }
}




