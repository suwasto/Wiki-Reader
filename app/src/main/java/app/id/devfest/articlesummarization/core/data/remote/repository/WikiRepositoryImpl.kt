package app.id.devfest.articlesummarization.core.data.remote.repository

import app.id.devfest.articlesummarization.core.data.local.Wiki
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WikiRepositoryImpl @Inject constructor() : WikiRepository {
    override fun getFeatured(): Flow<List<Wiki>> {
        TODO("Not yet implemented")
    }

    override fun searchWiki(query: String): Flow<List<Wiki>> {
        TODO("Not yet implemented")
    }

    override fun getAllBookmarks(): Flow<List<Wiki>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateWiki(wiki: Wiki) {
        TODO("Not yet implemented")
    }

}