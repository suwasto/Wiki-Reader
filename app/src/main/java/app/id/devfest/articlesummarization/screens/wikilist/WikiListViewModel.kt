package app.id.devfest.articlesummarization.screens.wikilist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.id.devfest.articlesummarization.core.data.local.Wiki
import app.id.devfest.articlesummarization.core.data.remote.repository.WikiRepository
import app.id.devfest.articlesummarization.screens.component.WikiListMapper
import coil.network.HttpException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WikiListViewModel @Inject constructor(
    private val repository: WikiRepository,
    private val mapper: WikiListMapper
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        WikiListUIState(
            wikiListUI = emptyList(), errorMsg = ""
        )
    )
    val uiState: StateFlow<WikiListUIState> = _uiState.asStateFlow()

    private val _uiEvents = MutableSharedFlow<WikiListUIEvent>(0)
    val uiEvents: SharedFlow<WikiListUIEvent> = _uiEvents.asSharedFlow()
    private var wikiJob: Job? = null

    init {
        getFeatured()
    }

    fun handleEvent(event: WikiListUIEvent) {
        viewModelScope.launch {
            _uiEvents.emit(event)
        }
    }

    fun searchWiki(query: String) {
        wikiJob?.cancel()
        wikiJob = viewModelScope.launch {
            repository.searchWiki(query)
                .onStart {
                    _uiState.value = _uiState.value.copy(
                        loading = true,
                        errorMsg = "",
                        wikiListUI = emptyList()
                    )
                }
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        errorMsg = e.message.toString(),
                        wikiListUI = emptyList()
                    )
                }
                .map {
                    mapper.mapToUIList(it)
                }
                .collect { wikilist ->
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        wikiListUI = wikilist,
                    )
                }
        }
    }

    fun getFeatured() {
        wikiJob?.cancel()
        wikiJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                loading = true,
                errorMsg = ""
            )
            delay(500)
            repository.getFeatured()
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        errorMsg = e.message.toString()
                    )
                }
                .map {
                    mapper.mapToUIList(it)
                }
                .collect { wikilist ->
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        wikiListUI = wikilist,
                    )
                }
        }
    }

    fun updateBookmark(
        wiki: Wiki
    ) {
        viewModelScope.launch {
            try {
                repository.updateWiki(wiki)
            } catch (_: Exception) {
            }
        }
    }

}