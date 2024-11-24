package app.id.devfest.articlesummarization.core.utils

import app.id.devfest.articlesummarization.core.data.remote.model.PageResponse
import app.id.devfest.articlesummarization.core.data.remote.model.QueryResponse
import app.id.devfest.articlesummarization.core.data.remote.model.WikiResponse
import com.google.gson.*
import java.lang.reflect.Type

class WikiResponseDeserializer : JsonDeserializer<WikiResponse> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): WikiResponse {
        val jsonObject = json.asJsonObject
        val queryObject = jsonObject.getAsJsonObject("query")
        val pagesObject = queryObject.getAsJsonObject("pages")

        val pageEntry = pagesObject.entrySet().firstOrNull()
        val page = pageEntry?.let {
            context.deserialize<PageResponse>(it.value, PageResponse::class.java)
        }

        return WikiResponse(QueryResponse(page))
    }
}