package com.ericafenyo.localstore

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ArticleDao {

    @Query("SELECT * FROM article")
    suspend fun getArticles(): List<Article>

    @Insert
    suspend fun bulkInsert(vararg articles: Article)

    @Delete
    suspend fun delete(article: Article)
}