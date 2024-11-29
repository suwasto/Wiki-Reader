package app.id.devfest.articlesummarization.core.utils

import app.id.devfest.articlesummarization.core.data.remote.model.Categorymember
import app.id.devfest.articlesummarization.core.data.remote.model.FeaturedResponse
import app.id.devfest.articlesummarization.core.data.remote.model.Query
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class FeaturedArticlesDeserializer : JsonDeserializer<FeaturedResponse> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext
    ): FeaturedResponse {
        val jsonObject = json.asJsonObject

        // Deserialize the categorymembers array
        val categoryMembersJsonArray = jsonObject
            .getAsJsonObject("query")
            .getAsJsonArray("categorymembers")

        // Filter out items where ns != 0
        val filteredCategoryMembers = categoryMembersJsonArray
            .mapNotNull { element ->
                val member =
                    context.deserialize<Categorymember>(element, Categorymember::class.java)
                if (member.ns == 0) {
                    member
                } else {
                    null
                }
            }

        // Create and return the FeaturedArticlesResponse object with filtered members
        val query = Query(filteredCategoryMembers)
        return FeaturedResponse(query)
    }
}

