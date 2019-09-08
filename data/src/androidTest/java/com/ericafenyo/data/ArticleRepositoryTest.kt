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

@file:JvmName("ArticleRepositoryTest")

package com.ericafenyo.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.ericafenyo.data.article.ArticleRepository
import com.ericafenyo.data.article.ArticleRepositoryImpl
import com.ericafenyo.data.article.source.ArticleRemoteDataSourceImlp
import com.ericafenyo.data.article.source.Section
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.withContext
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ArticleRepositoryTest {

  @get:Rule
  // Make all live data calls sync
  val instantExecutor = InstantTaskExecutorRule()

//  @Mock
//  lateinit var remoteDataSource: ArticleRemoteDataSource

  // Get a reference to the class under test.
  private lateinit var repository: ArticleRepository

  // private lateinit var articleDao: ArticleDao

  private val testScope = TestCoroutineScope()

  @Before
  fun setUp() {
    // Bind mocks to this class.
//    MockitoAnnotations.initMocks(this)

    // Get a reference to a context object
    // This is needed to create the room database.
    val context = InstrumentationRegistry.getInstrumentation().targetContext

    //initialize objects

  }


  @Test
  fun getArticles(): Unit = runBlocking {

    val articleService = News().getArticleService(env.NEWS_API_KEY)

    val dataSource = ArticleRemoteDataSourceImlp(articleService, Mapper())

    repository = ArticleRepositoryImpl(dataSource)

    val articles = withContext(testScope.coroutineContext) {
      repository.getArticles(Section.business)
    }
  }


  @Test
  fun shouldPass() {
    println("Articles")
    assert(true)
  }
}