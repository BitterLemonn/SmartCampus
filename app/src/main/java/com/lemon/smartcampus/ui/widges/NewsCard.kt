package com.lemon.smartcampus.ui.widges

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lemon.smartcampus.data.entities.NewsEntity
import com.lemon.smartcampus.ui.theme.TextLightDay

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
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick.invoke() })
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
                        .background(Color(0xFFC4C4C4)),
                )
            } else {
                // TODO
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
                            .background(Color(0xFFDADADA)),
                    )
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(18.dp)
                            .background(Color(0xFFEAEAEA)),
                    )
                } else {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = date,
                        fontSize = 12.sp,
                        color = TextLightDay,
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
                NewsCard(imageUrl = item.imageUrl, title = item.title, date = item.date) {
                    onClick.invoke(data.indexOf(item))
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        } else for (x in 0..3) item {
            NewsCard(unload = true) {}
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
