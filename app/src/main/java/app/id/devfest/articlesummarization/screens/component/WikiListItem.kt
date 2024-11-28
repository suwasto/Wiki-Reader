package app.id.devfest.articlesummarization.screens.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.id.devfest.articlesummarization.R
import coil.compose.AsyncImage

@Composable
fun WikiListItem(
    title: String,
    description: String,
    thumbnailUrl: String?,
    isBookmark: Boolean,
    onClick: () -> Unit,
    onClickBookmark: () -> Unit
) {

    ElevatedCard(
        modifier = Modifier
            .wrapContentHeight()
            .wrapContentWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
        onClick = onClick,
    ) {


        Box(
            Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            if (!thumbnailUrl.isNullOrBlank()) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    model = thumbnailUrl,
                    contentDescription = null
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.ic_placeholder),
                    contentDescription = "No Thumbnail",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(Color.White),
                )
            }
            IconButton(
                modifier = Modifier.align(Alignment.TopEnd),
                onClick = {
                    onClickBookmark()
                }
            ) {
                val bookmark = if (isBookmark) R.drawable.ic_bookmark_filled else R.drawable.ic_bookmark_border
                Image(
                    painter = painterResource(bookmark),
                    contentDescription = "bookmark",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                )
            }
        }

        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                fontSize = 16.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun WikiListItemPreview() {
    WikiListItem(
        title = "This is tittle",
        description = "This is description",
        null,
        false,
        onClick = {}
    ) { }
}