package app.id.devfest.articlesummarization.screens.wikilist

import app.id.devfest.articlesummarization.screens.component.WikiListUI

data class WikiListUIState(
    val wikiListUI: List<WikiListUI>,
    val errorMsg: String,
    val loading: Boolean = true,
    val selectedItem: WikiListUI? = null
)
