package app.id.devfest.articlesummarization.screens.wikilist

import app.id.devfest.articlesummarization.screens.component.WikiListUI

data class WikiListUIState(
    var wikiListUI: List<WikiListUI>,
    var errorMsg: String,
    var loading: Boolean = true,
    var selectedItem: WikiListUI? = null
)
