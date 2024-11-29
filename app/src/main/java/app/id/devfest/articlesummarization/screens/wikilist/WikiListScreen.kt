package app.id.devfest.articlesummarization.screens.wikilist

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import app.id.devfest.articlesummarization.R
import app.id.devfest.articlesummarization.core.data.local.Wiki
import app.id.devfest.articlesummarization.screens.component.WikiListItem
import app.id.devfest.articlesummarization.screens.component.WikiListUI
import app.id.devfest.articlesummarization.screens.wikidetail.WikiDetailScreen
import app.id.devfest.articlesummarization.ui.theme.ArticleSummarizationTheme

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun WikiListScreen(
    viewModel: WikiListViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val navigator = rememberListDetailPaneScaffoldNavigator<WikiListUI>()
    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvents.collect { event ->
            when (event) {
                is WikiListUIEvent.Refresh -> {
                    viewModel.getFeatured()
                }

                is WikiListUIEvent.UpdateBookmark -> {
                    viewModel.updateBookmark(wiki = event.wiki)
                }

                is WikiListUIEvent.NavigateToDetail -> {
                    viewModel.setSelectedItem(event.wikiListUI)
                    navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, event.wikiListUI)
                }

                is WikiListUIEvent.ShowToast -> {
                    Toast.makeText(context, event.msg, Toast.LENGTH_SHORT).show()
                }

                is WikiListUIEvent.Search -> {
                    viewModel.searchWiki(event.query)
                }

                is WikiListUIEvent.OnBackFromDetail -> {
                    if (navigator.canNavigateBack()) {
                        navigator.navigateBack()
                    }
                }
            }
        }
    }

    WikiListDetailPane(
        navigator = navigator,
        state = state,
        onEvent = {
            viewModel.handleEvent(it)
        }
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun WikiListDetailPane(
    navigator: ThreePaneScaffoldNavigator<WikiListUI>,
    state: WikiListUIState,
    onEvent: (WikiListUIEvent) -> Unit,
) {
    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                WikiList(
                    state = state,
                    onEvent = onEvent
                )
            }
        },
        detailPane = {
            AnimatedPane {
                state.selectedItem?.let { selectedItem ->
                    WikiDetailScreen(
                        wikiListUI = selectedItem,
                        backHandler = {
                            onEvent(
                                WikiListUIEvent.OnBackFromDetail
                            )
                        }
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WikiList(
    state: WikiListUIState,
    onEvent: (WikiListUIEvent) -> Unit
) {
    var refreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullToRefreshState()
    PullToRefreshBox(
        modifier = Modifier
            .fillMaxSize(),
        isRefreshing = refreshing,
        onRefresh = {
            onEvent(WikiListUIEvent.Refresh)
        },
        state = pullRefreshState
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(360.dp),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
        ) {

            item(span = { GridItemSpan(this.maxLineSpan) }) {
                HeaderSection(
                    onEvent = onEvent
                )
            }

            // Error Message
            if (state.errorMsg.isNotBlank()) {
                item(span = { GridItemSpan(this.maxLineSpan) }) {
                    ErrorSection(state.errorMsg)
                }
            }

            // Loading Indicator
            refreshing = state.loading && state.wikiListUI.isNotEmpty()
            if (state.loading && state.wikiListUI.isEmpty()) {
                item(span = { GridItemSpan(this.maxLineSpan) }) {
                    LoadingIndicator()
                }
            }


            // Wiki List Items
            if (state.wikiListUI.isNotEmpty()) {
                items(state.wikiListUI) { item ->
                    WikiListItem(
                        title = item.title,
                        description = item.content,
                        thumbnailUrl = item.thumbnail,
                        isBookmark = item.isBookmark,
                        onClick = {
                            onEvent(
                                WikiListUIEvent.NavigateToDetail(
                                    item
                                )
                            )
                        },
                        onClickBookmark = {
                            onEvent(
                                WikiListUIEvent.UpdateBookmark(
                                    Wiki(
                                        title = item.title,
                                        content = item.content,
                                        thumbnailUrl = item.thumbnail,
                                        isBookmarked = !item.isBookmark
                                    )
                                )
                            )
                        }
                    )
                }
            }

            // Empty State Message
            if (!state.loading && state.wikiListUI.isEmpty() && state.errorMsg.isBlank()) {
                item(span = { GridItemSpan(this.maxLineSpan) }) {
                    EmptyStateMessage()
                }
            }

        }
    }

}



@Composable
private fun HeaderSection(
    onEvent: (WikiListUIEvent) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.height(100.dp),
            painter = painterResource(R.drawable.ic_placeholder),
            contentDescription = null
        )

        SearchBar(
            modifier = Modifier.padding(16.dp),
            onSearch = {
                onEvent(
                    WikiListUIEvent.Search(it)
                )
            }
        )

        Text(
            text = "Curious about something?",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        Text(
            text = "Search above to discover more on Wikipedia!",
            fontSize = 14.sp
        )
    }
}

@Composable
private fun ErrorSection(errorMessage: String) {
    Text(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        text = "Something went wrong!\n$errorMessage\nPull to refresh",
        color = MaterialTheme.colorScheme.error,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun LoadingIndicator() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun EmptyStateMessage() {
    Text(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        text = "No wiki found",
        textAlign = TextAlign.Center
    )
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    ElevatedCard(
        modifier = modifier
            .widthIn(0.dp, 360.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        ),
        elevation = CardDefaults.elevatedCardElevation(1.dp)
    ) {
        OutlinedTextField(
            value = query,
            maxLines = 1,
            onValueChange = { query = it },
            placeholder = { Text("Search wiki...") },
            leadingIcon = {
                Icon(imageVector = Icons.Rounded.Search, contentDescription = "Search Icon")
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { query = "" }) {
                        Icon(imageVector = Icons.Rounded.Clear, contentDescription = "Clear Icon")
                    }
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            modifier = Modifier.fillMaxWidth(),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch(query)
                    keyboardController?.hide()
                }
            ),
            shape = RoundedCornerShape(12.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWikiList() {
    ArticleSummarizationTheme {
        WikiList(
            state = WikiListUIState(
                emptyList(), ""
            ),
            onEvent = {}
        )
    }
}

