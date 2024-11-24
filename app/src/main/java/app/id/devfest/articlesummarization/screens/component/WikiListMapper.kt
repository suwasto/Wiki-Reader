package app.id.devfest.articlesummarization.screens.component

import app.id.devfest.articlesummarization.core.data.local.Wiki

class WikiListMapper {

    private fun mapToUI(wiki: Wiki): WikiListUI {
        return WikiListUI(
            title = wiki.title,
            content = wiki.content,
            thumbnail = wiki.thumbnailUrl,
            isBookmark = wiki.isBookmarked
        )
    }

    fun mapToUIList(wikis: List<Wiki>): List<WikiListUI> {
        return wikis.map { mapToUI(it) }
    }

}