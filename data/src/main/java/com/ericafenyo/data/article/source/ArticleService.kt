package com.ericafenyo.data.source

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ArticleService {
  /**
   * Returns an array of articles currently on the specified section.
   *
   * @param section The section the story appears in. Allow values:
   * arts, automobiles, books, business, fashion, food, health, home,
   * insider, magazine, movies, national, nyregion, obituaries, opinion,
   * politics, realestate, science, sports, sundayreview, technology,
   * theater, tmagazine, travel, upshot, world.
   *
   * @return A [Deferred] of [ArticleService].
   */
  @GET("{section}.json")
  fun fetchTopStories(
      @Path("section") section: String
  ): Deferred<ArticleDTO>


  /**
   *
   */
  @GET("articlesearch.json")
  fun search(
      @Query("q") query: String,
      @Query("fq") filter: String
  ): Deferred<ArticleDTO>
}