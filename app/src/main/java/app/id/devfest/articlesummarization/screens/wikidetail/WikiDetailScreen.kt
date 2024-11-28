package app.id.devfest.articlesummarization.screens.wikidetail

import androidx.compose.animation.animateColor
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import app.id.devfest.articlesummarization.R
import app.id.devfest.articlesummarization.screens.component.WikiListUI
import coil.compose.AsyncImage

@Composable
fun WikiDetailScreen(
    wikiListUI: WikiListUI,
    backHandler: () -> Unit,
    viewModel: WikiDetailViewModel = hiltViewModel(),
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
) {
    LaunchedEffect(wikiListUI) {
        viewModel.sendPrompt(wikiListUI.content)
    }

    val state by viewModel.uiState.collectAsState()

    WikiDetail(
        state = state,
        wikiListUI = wikiListUI,
        popNavigation = backHandler,
        windowSizeClass
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WikiDetail(
    state: WikiDetailUiState,
    wikiListUI: WikiListUI,
    popNavigation: () -> Unit,
    windowSizeClass: WindowSizeClass
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        text = wikiListUI.title,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT) {
                        IconButton(
                            onClick = {
                                popNavigation.invoke()
                            }
                        ) {
                            Icon(painter = painterResource(R.drawable.baseline_arrow_back_24), "")
                        }
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            if (wikiListUI.thumbnail.isBlank()) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(2f),
                    painter = painterResource(R.drawable.ic_placeholder),
                    contentDescription = "wiki image"
                )
            } else {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(2f),
                    model = wikiListUI.thumbnail,
                    contentDescription = "wiki image"
                )
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .animateContentSize(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp),
                    text = when {
                        state.loading -> {
                            "Generating summary..."
                        }

                        state.errorMsg.isNotBlank() -> {
                            "Error when generating summary"
                        }

                        else -> "Summary"
                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (state.loading) {
                    val infiniteTransition = rememberInfiniteTransition(label = "")

                    val shimmerColor by infiniteTransition.animateColor(
                        initialValue = MaterialTheme.colorScheme.surfaceContainerHigh,
                        targetValue = MaterialTheme.colorScheme.surfaceContainerHighest,
                        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse),
                        label = ""
                    )
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(16.dp)
                            .padding(horizontal = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = shimmerColor
                        )
                    ) {}
                } else {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(top = 8.dp, bottom = 16.dp),
                        text = state.summary.ifBlank { state.errorMsg },
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                text = wikiListUI.content,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun WikiDetailSuccessPreview() {
    WikiDetail(
        WikiDetailUiState(),
        WikiListUI(
            "This is the thumbnail", "", "", true
        ),
        {},
        currentWindowAdaptiveInfo().windowSizeClass
    )
}
