package com.ericafenyo.repository

import com.ericafenyo.data.entities.Article
import com.ericafenyo.data.news.ArticleResponse
import com.ericafenyo.data.news.News
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

/**
 * Interface that allows controlling a [LiveData] from a coroutine block.
 *
 * @see liveData
 */
interface NewsRepository {
    /**
     * Returns an array of articles currently on the specified section.
     *
     * @param section The section the story appears in. Allowed values:
     * arts, automobiles, books, business, fashion, food, health, home,
     * insider, magazine, movies, national, nyregion, obituaries, opinion,
     * politics, realestate, science, sports, sundayreview, technology,
     * theater, tmagazine, travel, upshot, world.
     *
     * @return A [List] of [Article].
     */
    suspend fun getTopStories(section: String): List<Article>

    suspend fun search(query: String, filter: String): List<Article>
}


 class NewsRepositoryImpl : NewsRepository {
    private val news = News("ZzukbrrwPGs9k4M5gSk0SGzMGRb40BQB")
    private val articleService = news.articleService

    override suspend fun getTopStories(section: String): List<Article> {
        return withContext(Dispatchers.IO) {
            try {
                articleService.fetchTopStories(section).await().transform(::toArticles)
            } catch (e: Exception) {
                if (e is HttpException) {
                    print(e.response().raw().request().url())
                }
                listOf<Article>()
            }
        }
    }

    private fun toArticles(response: ArticleResponse): List<Article> {
        return response.results.map(::buildArticle)
    }

    private fun buildArticle(response: ArticleResponse.Result): Article = with(response) {
        Article.Builder()
            .title(title)
            .section(section)
            .description(abstract)
            .imageUrl(getMediaUrl(multimedia))
            .articleUrl(shortUrl)
            .publishedDate(publishedDate)
            .author(byline)
            .build()
    }

    private fun getMediaUrl(multimedia: List<ArticleResponse.Result.Multimedia>): String {
        return if (multimedia.isEmpty()) "" else multimedia.last().url
    }

    override suspend fun search(query: String, filter: String): List<Article> {
        TODO("not implemented")
    }
}

fun <T : Any, R : Any> T.transform(transformer: (T) -> R): R = transformer.invoke(this)
