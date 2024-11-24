package app.id.devfest.articlesummarization.core.data.remote.model


import com.google.gson.annotations.SerializedName

data class FeaturedResponse(
    @SerializedName("query")
    var query: Query
)

data class Query(
    @SerializedName("categorymembers")
    var categorymembers: List<Categorymember>
)

data class Categorymember(
    @SerializedName("ns")
    var ns: Int?,
    @SerializedName("pageid")
    var pageid: Int?,
    @SerializedName("title")
    var title: String
)