package app.id.devfest.articlesummarization.screens.component

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WikiListUI(
    val title: String,
    val content: String,
    val thumbnail: String,
    val isBookmark: Boolean
): Parcelable