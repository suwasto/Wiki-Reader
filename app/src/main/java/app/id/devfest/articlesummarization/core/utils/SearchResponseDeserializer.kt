package app.id.devfest.articlesummarization.core.utils

import app.id.devfest.articlesummarization.core.data.remote.model.SearchResponse
import com.google.gson.*
import java.lang.reflect.Type

class SearchResponseDeserializer : JsonDeserializer<SearchResponse> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): SearchResponse {
        val jsonArray = json.asJsonArray
        // Check if the jsonArray has at least two elements
        val titlesArray = if (jsonArray.size() > 1) jsonArray[1].asJsonArray else JsonArray()
        // Convert titlesArray to a list of strings
        val titles = titlesArray.map { it.asString }
        return SearchResponse(titles)
    }
}