package com.lemon.smartcampus.ui.discoverPage.tabPage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lemon.smartcampus.data.entities.TopicEntity
import com.lemon.smartcampus.ui.theme.TextLightDay
import com.lemon.smartcampus.ui.widges.LazyLoadMoreColumn
import com.lemon.smartcampus.ui.widges.ResCard
import com.lemon.smartcampus.ui.widges.TopicCard

@Composable
fun ResPage(navController: NavController?, needToTop: Boolean = false, onScrollTop: () -> Unit) {
    var loadState by remember { mutableStateOf(false) }
    val topicList = remember { mutableStateListOf<TopicEntity>() }
    val lazyState = rememberLazyListState()

    LaunchedEffect(key1 = needToTop) {
        if (needToTop)
            lazyState.scrollToItem(0)
        onScrollTop.invoke()
    }

    if (topicList.isEmpty())
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "还没有人发布资源哦,快和大家分享吧~",
                color = TextLightDay,
                fontSize = 16.sp
            )
        }
    else LazyLoadMoreColumn(loadState = loadState, onLoad = { /*TODO*/ }) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = lazyState
        ) {
            topicList.forEach {
                item {
                    TopicCard(
                        iconUrl = it.iconUrl,
                        nickName = it.nickName,
                        date = it.date,
                        content = it.content,
                        tag = it.tags,
                        hasRes = true,
                        resCard = {
                            ResCard(
                                resName = it.resName,
                                resType = it.resType,
                                resLink = it.resLink,
                                resSize = it.resSize,
                                onDownload = {}
                            )
                        }
                    ) { // onClick

                    }
                }
            }
        }
    }
}