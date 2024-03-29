package com.lemon.smartcampus.ui.widges

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.lemon.smartcampus.R
import com.lemon.smartcampus.ui.theme.AppTheme

@Composable
fun ProfileCard(
    iconUrl: String,
    nickname: String,
    tags: List<String>,
    onEdit: () -> Unit,
    onChangeArthur: () -> Unit
) {
    Box(modifier = Modifier.width(300.dp), contentAlignment = Alignment.TopCenter) {
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .padding(top = 30.dp)
                .fillMaxWidth()
                .height(100.dp)
                .clip(RoundedCornerShape(10.dp))
                .clickable(
                    interactionSource = MutableInteractionSource(), indication = rememberRipple()
                ) { onEdit.invoke() },
            backgroundColor = if (!isSystemInDarkTheme()) Color(0xFFEAEAEA) else Color(0xFF2A2A2A),
            elevation = 10.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 40.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = nickname.ifBlank { "您还未登录哦~" },
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = AppTheme.colors.textBlackColor
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.width(200.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    tags.forEach { tag ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(AppTheme.colors.card)
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(text = tag, fontSize = 10.sp, color = AppTheme.colors.textDarkColor)
                        }
                    }
                }
            }
        }
        Card(shape = CircleShape,
            elevation = 10.dp,
            modifier = Modifier
                .clip(CircleShape)
                .clickable(
                    interactionSource = MutableInteractionSource(), indication = rememberRipple()
                ) { onChangeArthur.invoke() }) {
            if (iconUrl.isBlank())
                Image(
                    painter = painterResource(id = R.drawable.cat_logo),
                    contentDescription = "icon",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            else
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(iconUrl).build(),
                    contentDescription = "icon",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
        }
    }
}

@Composable
@Preview
private fun ProfileCardPreview() {
    ProfileCard("", "昵称昵称昵称", listOf("软件工程", "2019级"), onEdit = {}, onChangeArthur = {})
}