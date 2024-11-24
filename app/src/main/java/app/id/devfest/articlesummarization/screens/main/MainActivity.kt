package app.id.devfest.articlesummarization.screens.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import app.id.devfest.articlesummarization.ui.theme.ArticleSummarizationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArticleSummarizationTheme {
                WikiBottomBar()
            }
        }
    }
}