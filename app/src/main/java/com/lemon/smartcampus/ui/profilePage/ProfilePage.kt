package com.lemon.smartcampus.ui.profilePage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.lemon.smartcampus.R
import com.lemon.smartcampus.data.entities.ProfileEntity
import com.lemon.smartcampus.data.entities.TopicEntity
import com.lemon.smartcampus.data.globalData.ProfileInfo
import com.lemon.smartcampus.ui.theme.TextDarkDay
import com.lemon.smartcampus.ui.widges.ProfileBtn
import com.lemon.smartcampus.ui.widges.ProfileCard
import com.lemon.smartcampus.ui.widges.ProfileResCard

@Composable
fun ProfilePage(navController: NavController?) {
    val profile: ProfileEntity = ProfileInfo.profile!!
    var resList = remember {
        mutableStateListOf<TopicEntity>(
            TopicEntity(
                "",
                "",
                "",
                "",
                listOf(),
                0,
                true,
                "文件名文件名",
                resSize = 100.0f,
                resLink = ""
            ),
            TopicEntity(
                "",
                "",
                "",
                "",
                listOf(),
                0,
                true,
                "文件名文件名",
                resSize = 100.0f,
                resLink = ""
            )
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = rememberRipple()
                ) {

                }) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(profile.bgImgUrl)
                        .build(),
                    contentDescription = "bg",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = maxWidth * (9 / 32f) + 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfileCard(iconUrl = profile.iconUrl,
                    nickname = profile.nickname,
                    tags = profile.tags,
                    onEdit = {},
                    onChangeArthur = {})
                Spacer(modifier = Modifier.height(20.dp))
                // 按钮组
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ProfileBtn(
                        img = R.drawable.calendar2,
                        text = "我的课程",
                        backgroundColor = Color(0xFFFFF3D8)
                    ) {

                    }
                    ProfileBtn(
                        img = R.drawable.memo, text = "我的备忘", backgroundColor = Color(0xFFD0F2FF)
                    ) {

                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                // 我的发布
                Column(
                    modifier = Modifier
                        .padding(horizontal = 23.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFEDEDED))
                ) {
                    if (resList.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp, top = 5.dp)
                        ) {
                            Text(text = "我的发布", fontSize = 12.sp, color = TextDarkDay)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp, end = 10.dp, bottom = 20.dp)
                        ) {
                            items(resList) { res ->
                                ProfileResCard(
                                    resName = res.resName,
                                    resType = res.resType,
                                    resSize = res.resSize,
                                    resLink = res.resLink,
                                    onClick = {},
                                    onDownload = {}
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                            }
                        }
                    }
                }
            }
        }
    }

}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFAFAFA)
fun ProfilePagePreview() {
    ProfilePage(null)
}