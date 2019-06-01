package com.ericafenyo.data.news

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ArticleService {
    /**
     * Get the basic movie information for a specific movie id.
     *
     * @param movieId          A Movie TMDb id.
     * @param language         *Optional.* ISO 639-1 code.
     * @param appendToResponse *Optional.* extra requests to append to the result. **Accepted Value(s):** alternative_titles, changes, credits, images, keywords, release_dates, videos, translations, recommendations, similar, reviews, lists
     */
    @GET("movie/{movie_id}")
    fun fetchTopStories(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String
    ): Deferred<ArticleResponse>


    /**
     * Get the basic movie information for a specific movie id.
     *
     * @param movieId          A Movie TMDb id.
     * @param language         *Optional.* ISO 639-1 code.
     * @param appendToResponse *Optional.* extra requests to append to the result. **Accepted Value(s):** alternative_titles, changes, credits, images, keywords, release_dates, videos, translations, recommendations, similar, reviews, lists
     */
    @GET("movie/{movie_id}")
    fun search(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String
    ): Deferred<ArticleResponse>
}