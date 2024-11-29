package app.id.devfest.articlesummarization.core.data.remote.repository

import app.id.devfest.articlesummarization.core.data.local.Wiki
import app.id.devfest.articlesummarization.core.data.local.WikiDao
import app.id.devfest.articlesummarization.core.data.remote.api.WikipediaApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WikiRepositoryImpl @Inject constructor(
    private val wikipediaApi: WikipediaApi,
    private val wikiDao: WikiDao
) : WikiRepository {

    override fun getFeatured(): Flow<List<Wiki>> = flow {
        val cachedData = wikiDao.getLast10Wiki().firstOrNull()
        if (cachedData.isNullOrEmpty()) {
            val featured = wikipediaApi.getFeatured()
            val categoryMembers = featured.query.categorymembers
            val wikiWithDetail = categoryMembers.map { member ->
                val pageInfo = wikipediaApi.getPageInfo(title = member.title)
                val page = pageInfo.query.page
                Wiki(
                    title = page?.title.orEmpty(),
                    content = page?.extract.orEmpty(),
                    thumbnailUrl = page?.thumbnail?.source.orEmpty(),
                )
            }
            wikiDao.insertAll(wikiWithDetail)
            emitAll(wikiDao.getLast10Wiki())
        } else {
            emitAll(wikiDao.getLast10Wiki())
        }
    }

    override fun searchWiki(query: String): Flow<List<Wiki>> = flow {
        val cachedData = wikiDao.getWikiByTitles(title = query).firstOrNull()
        if (cachedData.isNullOrEmpty()) {
            val searchResult = wikipediaApi.search(query = query)
            val titles = searchResult.titles
            val wikiWithDetail = titles.map { title ->
                val pageInfo = wikipediaApi.getPageInfo(title = title)
                val page = pageInfo.query.page
                Wiki(
                    title = page?.title.orEmpty(),
                    content = page?.extract.orEmpty(),
                    thumbnailUrl = page?.thumbnail?.source.orEmpty(),
                )
            }
            wikiDao.insertAll(wikiWithDetail)
            emitAll(wikiDao.getWikiByTitles(title = query))
        } else {
            emitAll(wikiDao.getWikiByTitles(title = query))
        }
    }

    override  fun getAllBookmarks(): Flow<List<Wiki>> = flow {
        emitAll(wikiDao.getAllBookmarkedWikis())
    }

    override suspend fun updateWiki(wiki: Wiki) {
        wikiDao.updateWiki(wiki)
    }
}

