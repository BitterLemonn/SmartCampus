package com.lemon.smartcampus.ui.widges

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.lemon.smartcampus.ui.theme.AppTheme
import java.text.DecimalFormat

@kotlinx.serialization.Serializable
object ResType {
    const val TXT = 0
    const val DOC = 1
    const val XLS = 2
    const val PPT = 3
    const val PSD = 4
    const val PNG = 5
    const val PDF = 6
    const val RAR = 7
    const val UNKNOWN = 8
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
        backgroundColor = if (!isSystemInDarkTheme()) Color.White else Color(0xFF2A2A2A),
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
                    if (iconUrl.isNotBlank()) AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current).data(iconUrl)
                            .crossfade(true).build(),
                        contentDescription = "$nickName icon",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        contentScale = ContentScale.Crop
                    )
                    else Image(
                        painter = painterResource(id = R.drawable.cat_logo),
                        contentDescription = "$nickName icon",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        contentScale = ContentScale.Crop
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
                            color = AppTheme.colors.textDarkColor,
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = date, color = AppTheme.colors.textDarkColor, fontSize = 10.sp
                        )
                    }
                }
                // 正文
                Spacer(modifier = Modifier.height(7.dp))
                Text(
                    text = content,
                    color = AppTheme.colors.textBlackColor,
                    maxLines = 3,
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp)
                )
                Spacer(modifier = Modifier.height(7.dp))
                if (hasRes) Box(modifier = Modifier.padding(bottom = 15.dp)) {
                    resCard.invoke()
                }
                // 工具栏
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = if (tag.isNotEmpty()) Arrangement.SpaceBetween
                    else Arrangement.End
                ) {
                    if (tag.isNotEmpty()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(0.5f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.tag),
                                contentDescription = "tag",
                                modifier = Modifier.size(20.dp)
                            )
                            tag.forEach {
                                Text(
                                    text = "#$it",
                                    color = AppTheme.colors.textLightColor,
                                    fontSize = 12.sp
                                )
                                Spacer(modifier = Modifier.width(15.dp))
                            }
                        }
                    }
                    Row(
                        modifier = Modifier.width(40.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "$commentCount",
                            color = AppTheme.colors.textLightColor,
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
                .background(AppTheme.colors.hintDarkColor)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(18.dp)
                    .background(AppTheme.colors.textLightColor)
            )
            Box(
                modifier = Modifier
                    .width(50.dp)
                    .height(12.dp)
                    .background(AppTheme.colors.hintLightColor)
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
                .background(AppTheme.colors.textDarkColor)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .background(AppTheme.colors.textDarkColor)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .height(12.dp)
                .background(AppTheme.colors.textDarkColor)
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
                    .background(AppTheme.colors.hintLightColor)
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
                    .background(AppTheme.colors.hintLightColor)
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
    resType: Int = ResType.UNKNOWN,
    resSize: Float = 0f,
    resLink: String = "",
    isCard: Boolean = false,
    isDownloaded: Boolean = false,
    onDownload: (String) -> Unit,
    isDownloading: Boolean = false
) {
    if (!isCard) Box(modifier = Modifier
        .border(
            width = 2.dp,
            color = AppTheme.colors.textLightColor,
            shape = RoundedCornerShape(10.dp)
        )
        .clip(RoundedCornerShape(10.dp))
        .clickable(
            enabled = !isDownloading && !isDownloaded,
            indication = rememberRipple(),
            interactionSource = remember { MutableInteractionSource() }
        ) { onDownload.invoke(resLink) }
        .padding(horizontal = 13.dp)
        .fillMaxWidth()
        .height(50.dp)) {
        ResContent(resName, resType, resSize, isDownloaded, isDownloading)
    }
    else Card(
        modifier = Modifier
            .padding(start = 13.dp, end = 13.dp, bottom = 10.dp)
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = 10.dp,
        backgroundColor = AppTheme.colors.card
    ) {
        Box(modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(
                enabled = !isDownloading && !isDownloaded,
                indication = rememberRipple(),
                interactionSource = MutableInteractionSource()
            ) { onDownload.invoke(resLink) })
        { ResContent(resName, resType, resSize, isDownloaded, isDownloading) }
    }
}

@Composable
fun ResContent(
    resName: String = "",
    resType: Int = ResType.UNKNOWN,
    resSize: Float = 0f,
    isDownloaded: Boolean = false,
    isDownloading: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(start = 10.dp),
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
                color = AppTheme.colors.textLightColor,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "${DecimalFormat("0.##").format(resSize)} MB",
                color = AppTheme.colors.textLightColor,
                fontSize = 12.sp,
                maxLines = 1
            )
            Spacer(modifier = Modifier.width(15.dp))
            if (isDownloaded) Image(
                painter = painterResource(id = R.drawable.right),
                contentDescription = "downloaded",
                modifier = Modifier.size(25.dp)
            )
            else if (!isDownloading) Image(
                painter = painterResource(id = R.drawable.download2),
                contentDescription = "download",
                modifier = Modifier.size(25.dp)
            )
        }
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFAFAFA)
private fun CardPreview() {
    TopicCard(isLoading = false,
        iconUrl = "",
        nickName = "昵称昵称昵称昵称昵称",
        date = "1980-01-01",
        content = "这是正文这是正文这是正文这是正文这是正文这是正文这是正文这是正文这是正文这是正文这是正文这是正文这是" + "正文这是正文这是正文这是正文这是正文这是正文这是正文",
        tag = listOf("学习", "安卓", "Compose"),
        hasRes = false,
        resCard = { ResCard(onDownload = {}) },
        onClick = { /*TODO*/ })
}