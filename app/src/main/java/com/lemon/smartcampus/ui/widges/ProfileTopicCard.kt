package com.lemon.smartcampus.ui.widges

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lemon.smartcampus.R
import com.lemon.smartcampus.ui.theme.AppTheme
import com.lemon.smartcampus.ui.theme.CardDay
import com.lemon.smartcampus.ui.theme.TextLightNight

@Composable
fun ProfileTopicCard(
    date: String,
    content: String,
    tags: List<String>,
    commentCount: Int,
    onDel: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = 10.dp,
        backgroundColor = if (isSystemInDarkTheme()) TextLightNight else CardDay
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    indication = rememberRipple(),
                    interactionSource = MutableInteractionSource(),
                    onClick = onClick
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp, horizontal = 5.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = date, fontSize = 10.sp, color = AppTheme.colors.textDarkColor)
                    Image(
                        painter = painterResource(id = R.drawable.del),
                        contentDescription = "delete",
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = rememberRipple(),
                                onClick = onDel
                            )
                            .padding(5.dp)
                    )
                }
                Text(
                    text = content,
                    maxLines = 3,
                    fontSize = 12.sp,
                    overflow = TextOverflow.Ellipsis,
                    color = AppTheme.colors.textDarkColor
                )
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 5.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(0.5f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (tags.isNotEmpty()) Image(
                            painter = painterResource(id = R.drawable.tag),
                            contentDescription = "tag",
                            modifier = Modifier.size(20.dp),
                            colorFilter = if (isSystemInDarkTheme()) ColorFilter.tint(Color.Gray) else null
                        )
                        tags.forEach {
                            Text(
                                text = "#$it",
                                color = AppTheme.colors.textDarkColor,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                        }
                    }
                    Row(
                        modifier = Modifier
                            .width(60.dp)
                            .padding(end = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$commentCount",
                            color = AppTheme.colors.textDarkColor,
                            fontSize = 12.sp,
                            modifier = Modifier.width(30.dp),
                            textAlign = TextAlign.End
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Image(
                            painter = painterResource(id = R.drawable.message),
                            contentDescription = "message count",
                            modifier = Modifier.size(20.dp),
                            colorFilter = if (isSystemInDarkTheme()) ColorFilter.tint(Color.Gray) else null
                        )
                    }
                }
            }

        }
    }
}

@Composable
@Preview
private fun ProfileTopicCardPreview() {
    ProfileTopicCard(date = "1980-01-01 00:00",
        content = "这是正文这是正文这是正文这是正文这是正文这是正文这是正文这是正文这是正文这是正文这是正文这是正文",
        tags = listOf("标签", "标签", "标签"),
        commentCount = 10,
        onDel = { /*TODO*/ }) {

    }
}