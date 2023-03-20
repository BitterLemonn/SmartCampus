package com.lemon.smartcampus.ui.profilePage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.lemon.smartcampus.R
import com.lemon.smartcampus.common.AUTH_PAGE
import com.lemon.smartcampus.data.entities.ProfileEntity
import com.lemon.smartcampus.data.entities.TopicEntity
import com.lemon.smartcampus.data.globalData.ProfileInfo
import com.lemon.smartcampus.ui.theme.SchoolBlueDay
import com.lemon.smartcampus.ui.theme.TextDarkDay
import com.lemon.smartcampus.ui.theme.TextLightDay
import com.lemon.smartcampus.ui.widges.ProfileBtn
import com.lemon.smartcampus.ui.widges.ProfileCard
import com.lemon.smartcampus.ui.widges.ProfileResCard
import com.lemon.smartcampus.ui.widges.ProfileTopicCard

@Composable
fun ProfilePage(navController: NavController?) {
    val profile: ProfileEntity = ProfileInfo.profile!!
    val resList = remember { mutableStateListOf<TopicEntity>() }
    val topicList = remember { mutableStateListOf<TopicEntity>() }

    Box(modifier = Modifier.fillMaxSize()) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            // 背景图
            Box(modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = rememberRipple()
                ) {
                    if (profile.id.isBlank()) {
                        navController?.navigate(AUTH_PAGE)
                    }
                }) {
                if (profile.bgImgUrl.isBlank()) Image(
                    painter = painterResource(id = R.drawable.unlogin_profile_bg),
                    contentDescription = "bg",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                else AsyncImage(
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
                    onEdit = {
                        if (profile.id.isBlank()) {
                            navController?.navigate(AUTH_PAGE)
                        }
                        // TODO 更改用户信息
                    },
                    onChangeArthur = {
                        if (profile.id.isBlank()) {
                            navController?.navigate(AUTH_PAGE)
                        }
                        // TODO 更改用户头像
                    })
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
                        // TODO 我的课程
                    }
                    ProfileBtn(
                        img = R.drawable.memo, text = "我的备忘", backgroundColor = Color(0xFFD0F2FF)
                    ) {
                        // TODO 我的备忘
                    }
                }
                // 我的发布
                if (resList.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 23.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFEDEDED))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp, top = 5.dp)
                        ) {
                            Text(text = "我的发布", fontSize = 14.sp, color = TextDarkDay)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp, end = 10.dp, bottom = 20.dp)
                        ) {
                            items(resList) { res ->
                                ProfileResCard(resName = res.resName,
                                    resType = res.resType,
                                    resSize = res.resSize,
                                    resLink = res.resLink,
                                    onClick = {
                                         // TODO 查看资源详情
                                    },
                                    onDownload = {
                                        // TODO 下载资源
                                    })
                                Spacer(modifier = Modifier.width(10.dp))
                            }
                        }
                    }
                }
                // 我的话题
                if (topicList.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 23.dp)
                            .fillMaxSize()
                            .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                            .background(Color(0xFFEDEDED))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp, top = 5.dp)
                        ) {
                            Text(text = "我的话题", fontSize = 14.sp, color = TextDarkDay)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 15.dp)
                        ) {
                            topicList.forEach { topic ->
                                item {
                                    ProfileTopicCard(date = topic.date,
                                        content = topic.content,
                                        tags = topic.tags,
                                        commentCount = topic.commentCount,
                                        onDel = { /*TODO 删除话题*/ }) {
                                        // TODO 查看话题详情
                                    }
                                    Spacer(modifier = Modifier.height(10.dp))
                                }
                            }
                        }
                    }
                } else if (resList.isEmpty() && topicList.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Image(
                                painter = painterResource(id = R.drawable.no_res),
                                contentDescription = "no any resource",
                                modifier = Modifier
                                    .fillMaxHeight(0.5f)
                                    .aspectRatio(1f)
                            )
                            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
                            Text(text = "快和大家分享你的发现吧!", fontSize = 16.sp, color = TextLightDay)
                            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .clickable(indication = rememberRipple(),
                                        interactionSource = MutableInteractionSource(),
                                        onClick = {
                                            if (profile.id.isBlank()) {
                                                navController?.navigate(AUTH_PAGE)
                                            }
                                            // TODO 发布话题
                                        })
                                    .background(SchoolBlueDay)
                                    .padding(vertical = 10.dp, horizontal = 20.dp)
                            ) {
                                Text(
                                    text = if (profile.id.isBlank()) "去登录" else "去创建话题",
                                    fontSize = 14.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp, end = 5.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable(indication = rememberRipple(),
                        interactionSource = MutableInteractionSource(),
                        onClick = {
                            // TODO 个人设置
                        }), contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.setting),
                    contentDescription = "setting",
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                )
            }
        }
    }

}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFAFAFA)
private fun ProfilePagePreview() {
    ProfilePage(null)
}