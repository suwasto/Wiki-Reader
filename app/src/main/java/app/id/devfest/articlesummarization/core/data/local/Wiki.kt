package app.id.devfest.articlesummarization.core.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("wiki")
data class Wiki(
    @PrimaryKey
    val title: String,
    val content: String,
    val thumbnailUrl: String,
    val isBookmarked: Boolean = false
)