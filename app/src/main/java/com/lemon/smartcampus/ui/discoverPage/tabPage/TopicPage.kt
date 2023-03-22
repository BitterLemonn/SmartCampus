package com.lemon.smartcampus.ui.discoverPage.tabPage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lemon.smartcampus.R
import com.lemon.smartcampus.data.database.entities.TopicEntity
import com.lemon.smartcampus.ui.theme.AppTheme
import com.lemon.smartcampus.ui.widges.LazyLoadMoreColumn
import com.lemon.smartcampus.ui.widges.TopicCard
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@Composable
fun TopicPage(navController: NavController?) {
    var loadState by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(true) }
    val topicList = remember { mutableStateListOf<TopicEntity>() }
    val lazyState = rememberLazyListState()
    var firstSeenItem by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = lazyState) {
        snapshotFlow { lazyState.firstVisibleItemIndex }.onEach { firstSeenItem = it }.collect()
    }

    if (topicList.isEmpty() && !isLoading) Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "还没有人发布话题哦,快和大家聊聊吧~", color = AppTheme.colors.textLightColor, fontSize = 16.sp
        )
    }
    else LazyLoadMoreColumn(loadState = loadState, onLoad = { /*TODO*/ }) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = lazyState
        ) {
            if (topicList.isEmpty()) items(3) {
                TopicCard(
                    isLoading = true,
                    iconUrl = "",
                    nickName = "",
                    date = "",
                    content = "",
                    tag = listOf()
                ) { // onClick

                }
            }
            else topicList.forEach {
                item {
                    TopicCard(
                        iconUrl = it.iconUrl,
                        nickName = it.nickName,
                        date = it.date,
                        content = it.content,
                        tag = it.tags
                    ) { // onClick

                    }
                }
            }
        }
    }
    if (firstSeenItem != 0) BoxWithConstraints(Modifier.fillMaxSize()) {
        FloatingActionButton(
            backgroundColor = Color(0xFF58BEFF),
            onClick = {
                scope.launch { lazyState.animateScrollToItem(0) }
            },
            modifier = Modifier
                .absolutePadding(left = maxWidth * 0.8f, top = maxHeight * 0.75f)
                .size(50.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.up),
                contentDescription = "up",
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFAFAFA)
private fun TopicPagePreview() {
    TopicPage(navController = null)
}