package app.id.devfest.articlesummarization.screens.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import app.id.devfest.articlesummarization.R
import app.id.devfest.articlesummarization.screens.wikibookmark.BookmarksScreen
import app.id.devfest.articlesummarization.screens.wikilist.WikiListScreen

@Composable
fun WikiBottomBar() {
    var currentDestination by rememberSaveable { mutableStateOf(BottomNavigationDestination.HOME) }
    NavigationSuiteScaffold(
        navigationSuiteItems = {
            BottomNavigationDestination.entries.forEach {
                item(
                    icon = {
                        Icon(
                            painter = painterResource(it.icon),
                            contentDescription = stringResource(it.label)
                        )
                    },
                    label = { Text(stringResource(it.label)) },
                    selected = it == currentDestination,
                    onClick = {
                        currentDestination = it
                    }
                )
            }
        },
        content = {
            when (currentDestination) {
                BottomNavigationDestination.HOME -> WikiListScreen()
                BottomNavigationDestination.BOOKMARKS -> BookmarksScreen()
            }
        },
        navigationSuiteColors = NavigationSuiteDefaults.colors(
            navigationBarContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            navigationRailContainerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    )
}

@Preview(showBackground = true)
@Composable
fun BottomBarPreview() {
    WikiBottomBar()
}

enum class BottomNavigationDestination(
    @DrawableRes val icon: Int,
    @StringRes val label: Int
) {
    HOME(
        R.drawable.rounded_home_24,
        R.string.menu_home
    ),
    BOOKMARKS(
        R.drawable.rounded_bookmarks_24,
        R.string.menu_bookmarks
    )
}
