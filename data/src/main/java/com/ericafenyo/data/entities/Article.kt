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

package com.ericafenyo.data.article.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A Room Entity used to create a Database table.
 * */
@Entity(tableName = "article")
data class Article(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val section: String,
    val description: String,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    @ColumnInfo(name = "article_url") val articleUrl: String,
    @ColumnInfo(name = "published_date") val publishedDate: String,
    val author: String
)