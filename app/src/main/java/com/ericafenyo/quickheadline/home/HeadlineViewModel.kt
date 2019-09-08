@file:JvmName("HeadlineViewModel")

package com.ericafenyo.quickheadline.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.ericafenyo.data.Mapper
import com.ericafenyo.data.News
import com.ericafenyo.data.article.ArticleRepositoryImpl
import com.ericafenyo.data.article.source.ArticleRemoteDataSourceImlp
import com.ericafenyo.data.article.source.Section
import com.ericafenyo.data.entities.Article
import com.ericafenyo.quickheadline.ScopedViewModel

class HeadlineViewModel : ScopedViewModel() {
  private val LOG_TAG = HeadlineViewModel::class.java.name


  private val articleService = News().getArticleService("")

  val dataSource = ArticleRemoteDataSourceImlp(articleService, Mapper())
  private val repository = ArticleRepositoryImpl(dataSource)

  val article: LiveData<List<Article>> = liveData {
    val data = repository.getArticles(Section.home)
    Log.w(LOG_TAG, "$data")
    emit(data)
  }
}