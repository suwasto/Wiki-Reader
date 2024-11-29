package app.id.devfest.articlesummarization.core.data.remote.api

import app.id.devfest.articlesummarization.core.data.remote.model.FeaturedResponse
import app.id.devfest.articlesummarization.core.data.remote.model.SearchResponse
import app.id.devfest.articlesummarization.core.data.remote.model.WikiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WikipediaApi {
    // API call for getting information about a specific page
    @GET("w/api.php")
    suspend fun getPageInfo(
        @Query("action") action: String = "query",
        @Query("titles") title: String,
        @Query("prop") prop: String = "pageimages|extracts",
        @Query("explaintext") explaintext: Int = 1,
        @Query("format") format: String = "json",
        @Query("origin") origin: String = "*",
        @Query("pithumbsize") pithumbsize: Int = 500
    ): WikiResponse
    // API call for searching articles by a query
    @GET("w/api.php")
    suspend fun search(
        @Query("action") action: String = "opensearch",
        @Query("search") query: String,
        @Query("limit") limit: Int = 10,
        @Query("namespace") namespace: Int = 0,
        @Query("format") format: String = "json",
        @Query("origin") origin: String = "*"
    ): SearchResponse
    // API call for getting featured articles
    @GET("w/api.php")
    suspend fun getFeatured(
        @Query("action") action: String = "query",
        @Query("list") list: String = "categorymembers",
        @Query("cmtitle") cmtitle: String = "Category:Featured_articles",
        @Query("cmlimit") cmlimit: Int = 10,
        @Query("format") format: String = "json",
        @Query("origin") origin: String = "*"
    ): FeaturedResponse
}

