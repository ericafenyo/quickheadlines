package com.ericafenyo.data.news


import com.google.gson.annotations.SerializedName

data class ArticleResponse(
    @SerializedName("copyright")
    val copyright: String,
    @SerializedName("last_updated")
    val lastUpdated: String,
    @SerializedName("num_results")
    val numResults: Int,
    @SerializedName("results")
    val results: List<Result>,
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
        @SerializedName("created_date")
        val createdDate: String,
        @SerializedName("des_facet")
        val desFacet: List<String>,
        @SerializedName("geo_facet")
        val geoFacet: List<Any>,
        @SerializedName("item_type")
        val itemType: String,
        @SerializedName("kicker")
        val kicker: String,
        @SerializedName("material_type_facet")
        val materialTypeFacet: String,
        @SerializedName("multimedia")
        val multimedia: List<Multimedia>,
        @SerializedName("org_facet")
        val orgFacet: List<Any>,
        @SerializedName("per_facet")
        val perFacet: List<Any>,
        @SerializedName("published_date")
        val publishedDate: String,
        @SerializedName("section")
        val section: String,
        @SerializedName("short_url")
        val shortUrl: String,
        @SerializedName("subsection")
        val subsection: String,
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