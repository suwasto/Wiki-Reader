package app.id.devfest.articlesummarization.screens.wikibookmark

import app.id.devfest.articlesummarization.screens.component.WikiListUI

data class BookmarkUiState(
    val loading: Boolean = false,
    val items: List<WikiListUI> = emptyList(),
    val errorMsg: String = ""
)