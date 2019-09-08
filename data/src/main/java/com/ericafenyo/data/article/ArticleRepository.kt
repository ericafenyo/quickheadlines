package com.ericafenyo.data.article

import androidx.lifecycle.MutableLiveData
import com.ericafenyo.data.NetworkState
import com.ericafenyo.data.Result
import com.ericafenyo.data.article.source.ArticleRemoteDataSource
import com.ericafenyo.data.article.source.Section
import com.ericafenyo.data.entities.Article

interface ArticleRepository {
  suspend fun search(query: String, filter: String): List<Article>
  suspend fun getArticles(section: Section): List<Article>
}

class ArticleRepositoryImpl constructor(
  private val remoteDataSource: ArticleRemoteDataSource
) : ArticleRepository {

  private val networkState = MutableLiveData<NetworkState>()

  override suspend fun search(query: String, filter: String): List<Article> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override suspend fun getArticles(section: Section): List<Article> {

    return when (val result = remoteDataSource.fetchTopStories(section)) {
      is Result.Success -> result.data
      is Result.Error -> emptyList()
    }
  }
}