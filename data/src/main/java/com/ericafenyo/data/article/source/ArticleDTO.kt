package com.ericafenyo.data.article.source


import com.google.gson.annotations.SerializedName

data class ArticleDTO(
  @SerializedName("copyright")
  val copyright: String,
  @SerializedName("last_updated")
  val lastUpdated: String,
  @SerializedName("num_results")
  val numResults: Int,
  @SerializedName("results")
  val articles: List<Result>,
  @SerializedName("section")
  val section: String,
  @SerializedName("status")
  val status: String
) {
  data class Result(
    @SerializedName("abstract")
    val `abstract`: String,
    @SerializedName("byline")
    val byline: String,
    @SerializedName("multimedia")
    val multimedia: List<Multimedia>?,
    @SerializedName("published_date")
    val publishedDate: String,
    @SerializedName("section")
    val section: String,
    @SerializedName("short_url")
    val shortUrl: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("updated_date")
    val updatedDate: String,
    @SerializedName("url")
    val url: String
  ) {
    data class Multimedia(
      @SerializedName("caption")
      val caption: String,
      @SerializedName("copyright")
      val copyright: String,
      @SerializedName("format")
      val format: String,
      @SerializedName("height")
      val height: Int,
      @SerializedName("subtype")
      val subtype: String,
      @SerializedName("type")
      val type: String,
      @SerializedName("url")
      val url: String,
      @SerializedName("width")
      val width: Int
    )
  }
}