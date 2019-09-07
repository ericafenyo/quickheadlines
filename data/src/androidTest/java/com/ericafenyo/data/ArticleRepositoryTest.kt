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
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ArticleRepositoryTest {

  @get:Rule
  // Make all live data calls sync
  val instantExecutor = InstantTaskExecutorRule()

  // Get a reference to the class under test.
  private lateinit var repository: ArticleRepository

  // private lateinit var articleDao: ArticleDao

  @Before
  fun setUp() {
    //Get a reference to a context object
    // This is needed to create the room database.
    val context = InstrumentationRegistry.getInstrumentation().targetContext

//    val database = Room.inMemoryDatabaseBuilder(context, PedometerDatabase::class.java).build()
//    stepDao = database.stepDao()
//
//    //initialize objects
//    repository = StepRepositoryImpl(stepDao)

    //TODO: Get reference to the ArticleDao

    //initialize objects
    repository = StepRepositoryImpl(stepDao)
  }

  @Test
  fun shouldPass() {
    assert(true);
  }
}