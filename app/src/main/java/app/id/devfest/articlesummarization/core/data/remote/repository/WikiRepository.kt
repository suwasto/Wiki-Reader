package app.id.devfest.articlesummarization.core.data.remote.repository

import app.id.devfest.articlesummarization.core.data.local.Wiki
import kotlinx.coroutines.flow.Flow

interface WikiRepository {

    fun getFeatured(): Flow<List<Wiki>>

    fun searchWiki(query: String): Flow<List<Wiki>>

    fun getAllBookmarks(): Flow<List<Wiki>>

    suspend fun updateWiki(wiki: Wiki)
}

