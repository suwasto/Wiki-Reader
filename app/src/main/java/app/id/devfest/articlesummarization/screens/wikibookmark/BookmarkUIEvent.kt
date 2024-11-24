package app.id.devfest.articlesummarization.screens.wikibookmark

import app.id.devfest.articlesummarization.screens.component.WikiListUI

sealed class BookmarkUIEvent {

    data class NavigateToDetail(val item: WikiListUI) : BookmarkUIEvent()

    data class ShowToast(val msg: String) : BookmarkUIEvent()

    data class DeleteBookmark(val wikiListUI: WikiListUI): BookmarkUIEvent()

    data object OnBackFromDetail: BookmarkUIEvent()

}