package app.id.devfest.articlesummarization.screens.wikibookmark

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.id.devfest.articlesummarization.screens.component.WikiListItem
import app.id.devfest.articlesummarization.screens.component.WikiListUI
import app.id.devfest.articlesummarization.screens.wikidetail.WikiDetailScreen
import app.id.devfest.articlesummarization.screens.wikilist.WikiList
import app.id.devfest.articlesummarization.screens.wikilist.WikiListUIEvent
import app.id.devfest.articlesummarization.screens.wikilist.WikiListUIState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun BookmarksScreen(
    viewModel: BookmarksViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val navigator = rememberListDetailPaneScaffoldNavigator<WikiListUI>()

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvents.collect { event ->
            when (event) {
                is BookmarkUIEvent.DeleteBookmark -> viewModel.deleteBookmark(event.wikiListUI)
                is BookmarkUIEvent.NavigateToDetail -> {
                    viewModel.setSelectedItem(event.item)
                    navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, event.item)
                }

                is BookmarkUIEvent.ShowToast -> {
                    Toast.makeText(context, event.msg, Toast.LENGTH_SHORT).show()
                }

                is BookmarkUIEvent.OnBackFromDetail -> {
                    if (navigator.canNavigateBack()) {
                        navigator.navigateBack()
                    }
                }
            }
        }
    }

    BookmarksListDetailPane(
        navigator = navigator,
        state = uiState,
        onEvent = { viewModel.handleEvent(it) }
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun BookmarksListDetailPane(
    navigator: ThreePaneScaffoldNavigator<WikiListUI>,
    state: BookmarkUiState,
    onEvent: (BookmarkUIEvent) -> Unit,
) {
    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                BookmarksList(
                    state = state,
                    onEvent = onEvent
                )
            }
        },
        detailPane = {
            state.selectedItem?.let { selectedItem ->
                AnimatedPane {
                    WikiDetailScreen(
                        wikiListUI = selectedItem,
                        backHandler = {
                            onEvent(BookmarkUIEvent.OnBackFromDetail)
                        }
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookmarksList(
    state: BookmarkUiState,
    onEvent: (BookmarkUIEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Bookmarks",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium
                    )
                },
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(360.dp),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.loading) {
                item { LoadingIndicator() }
            } else if (state.errorMsg.isNotBlank()) {
                item { ErrorMessage(state.errorMsg) }
            } else if (state.items.isEmpty()) {
                item { EmptyMessage() }
            } else {
                items(state.items, key = { item -> item.title }) { item ->
                    BookmarkItem(
                        item = item,
                        onEvent = onEvent
                    )
                }
            }
        }
    }
}

@Composable
private fun BookmarkItem(
    item: WikiListUI,
    onEvent: (BookmarkUIEvent) -> Unit
) {
    var isVisible by remember(item.title) {
        mutableStateOf(true)
    }
    AnimatedVisibility(isVisible) {
        WikiListItem(
            title = item.title,
            description = item.content,
            thumbnailUrl = item.thumbnail,
            isBookmark = item.isBookmark,
            onClick = {
                onEvent(BookmarkUIEvent.NavigateToDetail(item))
            },
            onClickBookmark = {
                isVisible = false
                CoroutineScope(Dispatchers.Main).launch {
                    delay(500) // Wait for animation to complete
                    onEvent(BookmarkUIEvent.DeleteBookmark(item))
                }
            }
        )
    }
}

@Composable
private fun LoadingIndicator() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorMessage(errorMsg: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = errorMsg)
    }
}

@Composable
private fun EmptyMessage() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.wrapContentSize(),
            text = "You have no bookmarks"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookmarksPreview() {
    BookmarksList(BookmarkUiState(loading = true)) { }
}