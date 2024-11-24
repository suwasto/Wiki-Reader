package app.id.devfest.articlesummarization.screens.wikilist

import app.id.devfest.articlesummarization.core.data.local.Wiki
import app.id.devfest.articlesummarization.screens.component.WikiListUI

sealed class WikiListUIEvent {
    data object Refresh: WikiListUIEvent()
    data class UpdateBookmark(val wiki: Wiki): WikiListUIEvent()
    data class NavigateToDetail(
        val wikiListUI: WikiListUI
    ): WikiListUIEvent()
    data class ShowToast(val msg: String): WikiListUIEvent()
    data class Search(val query: String): WikiListUIEvent()
    data object OnBackFromDetail: WikiListUIEvent()
}