package app.id.devfest.articlesummarization.screens.wikibookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.id.devfest.articlesummarization.core.data.local.Wiki
import app.id.devfest.articlesummarization.core.data.local.WikiDao
import app.id.devfest.articlesummarization.screens.component.WikiListMapper
import app.id.devfest.articlesummarization.screens.component.WikiListUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val wikiDao: WikiDao,
    private val mapper: WikiListMapper
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        BookmarkUiState()
    )
    val uiState: StateFlow<BookmarkUiState> = _uiState.asStateFlow()

    private val _uiEvents = MutableSharedFlow<BookmarkUIEvent>(0)
    val uiEvents: SharedFlow<BookmarkUIEvent> = _uiEvents.asSharedFlow()

    init {
        viewModelScope.launch {
            wikiDao.getAllBookmarkedWikis()
                .map { bookmarkedWikis ->
                    mapper.mapToUIList(bookmarkedWikis)
                }
                .onStart {
                    _uiState.value = _uiState.value.copy(
                        loading = true,
                        items = emptyList(),
                        errorMsg = ""
                    )
                }
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        items = emptyList(),
                        errorMsg = e.message.toString()
                    )
                }
                .collect { mappedBookmarks ->
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        items = mappedBookmarks
                    )
                }
        }
    }

    fun handleEvent(bookmarkUIEvent: BookmarkUIEvent) {
        viewModelScope.launch {
            _uiEvents.emit(bookmarkUIEvent)
        }
    }

    fun deleteBookmark(wikiListUI: WikiListUI) {
        viewModelScope.launch {
            val wiki = Wiki(
                title = wikiListUI.title,
                content = wikiListUI.content,
                thumbnailUrl = wikiListUI.thumbnail,
                isBookmarked = !wikiListUI.isBookmark
            )
            wikiDao.updateWiki(wiki)
        }
    }

}