package com.lemon.smartcampus.ui.widges

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.lemon.smartcampus.R
import com.lemon.smartcampus.ui.theme.HintDarkDay
import com.lemon.smartcampus.ui.theme.HintGrayDay
import com.lemon.smartcampus.ui.theme.TextDarkDay
import com.lemon.smartcampus.ui.theme.TextLightDay

enum class ResType {
    TXT, DOC, XLS, PPT, PSD, PNG, PDF, UNKNOWN
}

@Composable
fun TopicCard(
    isLoading: Boolean = false,
    iconUrl: String,
    nickName: String,
    date: String,
    content: String,
    tag: List<String>,
    commentCount: Int = 0,
    hasRes: Boolean = false,
    resCard: @Composable () -> Unit = {},
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .heightIn(max = if (!hasRes) 160.dp else 240.dp),
        backgroundColor = Color.White,
        elevation = 10.dp,
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    role = Role.Button,
                    onClick = onClick,
                    indication = rememberRipple(),
                    interactionSource = MutableInteractionSource()
                )
                .padding(horizontal = 13.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (!isLoading) {
                // 昵称信息栏
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current).data(iconUrl)
                            .crossfade(true).build(),
                        contentDescription = "$nickName icon",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(5.dp))
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = nickName,
                            color = TextLightDay,
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = date,
                            color = TextLightDay,
                            fontSize = 10.sp
                        )
                    }
                }
                // 正文
                Spacer(modifier = Modifier.height(7.dp))
                Text(
                    text = content,
                    color = TextDarkDay,
                    maxLines = 3,
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp)
                )
                Spacer(modifier = Modifier.height(7.dp))
                if (hasRes)
                    resCard.invoke()
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(0.5f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.tag),
                            contentDescription = "tag",
                            modifier = Modifier.size(20.dp)
                        )
                        tag.forEach {
                            Text(
                                text = "#$it",
                                color = TextLightDay,
                                fontSize = 12.sp
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(0.2f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "$commentCount",
                            color = TextLightDay,
                            fontSize = 12.sp
                        )
                        Image(
                            painter = painterResource(id = R.drawable.message),
                            contentDescription = "message count",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            } else TopicCardPlaceHolder()
        }
    }
}

@Composable
fun TopicCardPlaceHolder() {
    // 模拟昵称信息栏
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 11.dp, bottom = 5.dp)
            .height(40.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(HintDarkDay)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(18.dp)
                    .background(TextLightDay)
            )
            Box(
                modifier = Modifier
                    .width(50.dp)
                    .height(12.dp)
                    .background(HintGrayDay)
            )
        }
    }
    // 模拟文本栏
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .background(TextDarkDay)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .background(TextDarkDay)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .height(12.dp)
                .background(TextDarkDay)
        )
    }
    // 模拟工具栏
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // 模拟标签
        Row(
            modifier = Modifier.fillMaxWidth(0.4f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.tag),
                contentDescription = "tag",
                modifier = Modifier.size(20.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .background(HintGrayDay)
            )
        }
        // 模拟评论
        Row(
            modifier = Modifier.fillMaxWidth(0.25f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(30.dp)
                    .height(12.dp)
                    .background(HintGrayDay)
            )
            Image(
                painter = painterResource(id = R.drawable.message),
                contentDescription = "comment",
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun ResCard(
    resName: String = "",
    resType: ResType = ResType.UNKNOWN,
    resSize: Float = 0f,
    resLink: String = "",
    onDownload: (String) -> Unit,
    isDownloading: Boolean = false
) {
    Box(
        modifier = Modifier
            .border(
                width = 2.dp,
                color = Color(0xFFEAEAEA),
                shape = RoundedCornerShape(10.dp)
            )
            .clip(RoundedCornerShape(10.dp))
            .clickable(
                indication = rememberRipple(),
                interactionSource = MutableInteractionSource()
            ) {
                onDownload.invoke(resLink)
            }
            .padding(horizontal = 13.dp)
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(0.5f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.offical),
                    contentDescription = "icon",
                    modifier = Modifier
                        .size(35.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(15.dp))
                Text(
                    text = resName,
                    color = TextLightDay,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(0.5f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(text = "$resSize MB", color = TextLightDay, fontSize = 12.sp, maxLines = 1)
                Spacer(modifier = Modifier.width(15.dp))
                Image(
                    painter = painterResource(id = R.drawable.download2),
                    contentDescription = "icon",
                    modifier = Modifier.size(25.dp)
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFAFAFA)
fun CardPreview() {
    TopicCard(isLoading = true,
        iconUrl = "",
        nickName = "昵称昵称昵称昵称昵称",
        date = "1980-01-01",
        content = "这是正文这是正文这是正文这是正文这是正文这是正文这是正文这是正文这是正文这是正文这是正文这是正文这是" + "正文这是正文这是正文这是正文这是正文这是正文这是正文",
        tag = listOf("学习", "安卓", "Compose"),
        hasRes = false,
        resCard = { ResCard(onDownload = {}) },
        onClick = { /*TODO*/ })
}