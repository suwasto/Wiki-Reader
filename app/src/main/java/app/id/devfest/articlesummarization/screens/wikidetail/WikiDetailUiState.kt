package app.id.devfest.articlesummarization.screens.wikidetail

data class WikiDetailUiState(
    val loading: Boolean = false,
    val summary: String = "",
    val errorMsg: String = ""
)