package com.lemon.smartcampus.ui.widges

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.lemon.smartcampus.R
import com.lemon.smartcampus.data.database.entities.CommentEntity
import com.lemon.smartcampus.data.database.entities.TopicEntity
import com.lemon.smartcampus.ui.theme.AppTheme

@Composable
fun CommentHostCard(
    topic: TopicEntity,
    downloading: Boolean = false,
    onDownload: (String) -> Unit = {}
) {
    val nickname = topic.nickName

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .height(300.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // 正常显示
        if (nickname.isNotBlank()) {
            Column {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(5.dp))
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current).data(topic.iconUrl)
                                .build(), contentDescription = "${nickname}'s icon"
                        )
                    }
                    Spacer(modifier = Modifier.width(13.dp))
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .padding(vertical = 3.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = nickname,
                            fontSize = 14.sp,
                            color = AppTheme.colors.textLightColor
                        )
                        Text(
                            text = topic.date,
                            fontSize = 12.sp,
                            color = AppTheme.colors.textLightColor
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = topic.content, fontSize = 14.sp, color = Color.Black)
            }
            if (topic.hasRes)
                ResCard(
                    resName = topic.resName,
                    resType = topic.resType,
                    resSize = topic.resSize,
                    resLink = topic.resLink,
                    isDownloading = downloading,
                    isCard = true,
                    onDownload = onDownload
                )
            Column {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    LazyRow(verticalAlignment = Alignment.CenterVertically) {
                        item {
                            Image(
                                painter = painterResource(id = R.drawable.tag),
                                contentDescription = "tag",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                        }
                        items(topic.tags) {
                            Text(
                                text = "#$it",
                                fontSize = 12.sp,
                                color = AppTheme.colors.textLightColor
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                        }
                    }
                    Row(
                        Modifier.fillMaxWidth(0.3f),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${topic.commentCount}",
                            color = AppTheme.colors.textLightColor,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Image(
                            painter = painterResource(id = R.drawable.message),
                            contentDescription = "comment",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFEAEAEA),
                    thickness = 1.dp
                )
            }
        }
        // 加载占位
        else {
            Column {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(AppTheme.colors.hintDarkColor)
                    )
                    Spacer(modifier = Modifier.width(13.dp))
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .padding(vertical = 3.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .height(12.dp)
                                .width(120.dp)
                                .background(AppTheme.colors.textLightColor)
                        )
                        Box(
                            modifier = Modifier
                                .height(10.dp)
                                .width(80.dp)
                                .background(AppTheme.colors.hintLightColor)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                LazyColumn(Modifier.fillMaxWidth()) {
                    items(5) {
                        Box(
                            modifier = Modifier
                                .height(12.dp)
                                .fillMaxWidth(if (it != 4) 1f else 0.6f)
                                .background(AppTheme.colors.textLightColor)
                        )
                        if (it != 4) Spacer(modifier = Modifier.height(3.dp))
                    }
                }
            }
            Column {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.tag),
                            contentDescription = "tag",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Box(
                            modifier = Modifier
                                .width(120.dp)
                                .height(10.dp)
                                .background(AppTheme.colors.hintLightColor)
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .width(50.dp)
                                .height(10.dp)
                                .background(AppTheme.colors.hintLightColor)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Image(
                            painter = painterResource(id = R.drawable.message),
                            contentDescription = "comment",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFEAEAEA),
                    thickness = 1.dp
                )
            }
        }
    }
}

@Composable
fun CommentSubCard(
    comment: CommentEntity
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(comment.iconUrl).build(),
                contentDescription = "${comment.nickName}'s icon",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(13.dp))
            Column(
                Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .padding(vertical = 3.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = comment.nickName,
                    fontSize = 14.sp,
                    color = AppTheme.colors.textLightColor
                )
                Text(text = comment.date, fontSize = 12.sp, color = AppTheme.colors.textLightColor)
            }
        }
        Spacer(modifier = Modifier.height(11.dp))
        Text(text = comment.content, fontSize = 14.sp, color = Color.Black)
        Spacer(modifier = Modifier.height(20.dp))
        Divider(modifier = Modifier.fillMaxWidth(), color = Color(0xFFEAEAEA), thickness = 1.dp)
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFAFAFA)
private fun CommentHostCardPreview() {
//    CommentHostCard(
//        topic = TopicEntity.getEmpty()
//            .copy(nickName = "昵称昵称昵称", date = "1980-01-01", tags = listOf("学习", "安卓"))
//    )
    CommentSubCard(
        CommentEntity(
            nickName = "昵称昵称昵称", iconUrl = "", date = "1980-01-01", content =
            "正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文" +
                    "正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文"
        )
    )
}