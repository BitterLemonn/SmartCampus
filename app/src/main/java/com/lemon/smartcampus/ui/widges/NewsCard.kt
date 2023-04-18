package com.lemon.smartcampus.ui.widges

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.lemon.smartcampus.data.database.entities.NewsEntity
import com.lemon.smartcampus.ui.theme.AppTheme

@Composable
fun NewsCard(
    modifier: Modifier = Modifier,
    unload: Boolean = false,
    imageUrl: String = "",
    title: String = "",
    date: String = "1980-01-01",
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(90.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        elevation = 5.dp,
        backgroundColor = AppTheme.colors.card
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(10.dp))
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = rememberRipple()
            ) { onClick.invoke() })
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 13.dp, horizontal = 10.dp)
        ) {
            if (unload || imageUrl.isBlank()) {
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(64.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(AppTheme.colors.hintDarkColor),
                )
            } else {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(imageUrl).build(),
                    contentDescription = "news cover image",
                    modifier = Modifier
                        .width(100.dp)
                        .height(64.dp)
                        .clip(RoundedCornerShape(5.dp)),
                    contentScale = ContentScale.Crop,
                    colorFilter = if (isSystemInDarkTheme()) ColorFilter.tint(
                        Color.Gray,
                        blendMode = BlendMode.Multiply
                    ) else null
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 5.dp),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                if (unload) {
                    Box(
                        modifier = Modifier
                            .width(200.dp)
                            .height(18.dp)
                            .background(AppTheme.colors.hintDarkColor),
                    )
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(18.dp)
                            .background(AppTheme.colors.hintLightColor),
                    )
                } else {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = AppTheme.colors.textBlackColor
                    )
                    Text(
                        text = date.split(" ").first(),
                        fontSize = 12.sp,
                        color = AppTheme.colors.textDarkColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun NewsCardList(data: List<NewsEntity>, onClick: (Int) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(330.dp),
        contentPadding = PaddingValues(vertical = 10.dp)
    ) {
        if (data.isNotEmpty()) {
            for (item in data) item {
                NewsCard(
                    imageUrl = item.informationCover,
                    title = item.informationTitle,
                    date = item.publishDate
                ) {
                    onClick.invoke(data.indexOf(item))
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        } else items(4) {
            NewsCard(unload = true) { onClick.invoke(it) }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
@Preview
private fun NewsCardPreview() {
//    NewsCard(title = "“根系广外 绿美校园”128株沉香佳木根植广外") {}
    NewsCardList(data = listOf()) {}
}
