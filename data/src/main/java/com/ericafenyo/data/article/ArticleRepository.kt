package com.ericafenyo.data.article

import com.ericafenyo.data.article.source.ArticleRemoteDataSource
import com.ericafenyo.data.entities.Article

interface ArticleRepository {
  suspend fun search(query: String, filter: String): List<Article>
  suspend fun getArticles(section: String): List<Article>
}

class ArticleRepositoryImpl constructor(
  private val remoteDataSource: ArticleRemoteDataSource
) : ArticleRepository {
  override suspend fun search(query: String, filter: String): List<Article> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override suspend fun getArticles(section: String): List<Article> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}