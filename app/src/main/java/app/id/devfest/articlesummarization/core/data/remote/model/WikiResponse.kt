package app.id.devfest.articlesummarization.core.data.remote.model

import com.google.gson.annotations.SerializedName


data class WikiResponse(
    @SerializedName("query") val query: QueryResponse
)

data class QueryResponse(
    // No @SerializedName needed here since it's handled in the deserializer
    val page: PageResponse?
)

data class PageResponse(
    @SerializedName("pageid") val pageid: Long,
    @SerializedName("title") val title: String,
    @SerializedName("extract") val extract: String,
    @SerializedName("thumbnail") val thumbnail: ThumbnailResponse?
)

data class ThumbnailResponse(
    @SerializedName("source") val source: String
)