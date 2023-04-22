package com.lemon.smartcampus.ui.discoverPage.tabPage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.lemon.smartcampus.data.globalData.AppContext
import com.lemon.smartcampus.ui.theme.AppTheme
import com.lemon.smartcampus.ui.widges.SNACK_ERROR
import com.lemon.smartcampus.ui.widges.TopicCard
import com.lemon.smartcampus.viewModel.topic.TopicViewAction
import com.lemon.smartcampus.viewModel.topic.TopicViewModel
import com.orhanobut.logger.Logger
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TopicPage(
    showToast: (String, String) -> Unit = { _, _ -> },
    viewModel: TopicViewModel = viewModel(),
    scrollToTop: MutableState<Boolean> = mutableStateOf(false),
    navToPostDetail: (String) -> Unit,
    onChangeToTop: (Boolean) -> Unit,
) {
    val pageData = viewModel.getPage(true).collectAsLazyPagingItems()
    var refreshing by remember { mutableStateOf(false) }

    val pullRefreshState = rememberPullRefreshState(false, { pageData.refresh() }, 60.dp)
    val lazyState = rememberLazyListState()

    LaunchedEffect(key1 = scrollToTop.value) {
        if (scrollToTop.value) {
            lazyState.animateScrollToItem(0)
            scrollToTop.value = false
        }
    }

    LaunchedEffect(key1 = Unit) {
        snapshotFlow { pageData.loadState }.onEach { states ->
            refreshing = states.append is LoadState.Loading || states.refresh is LoadState.Loading
            if (states.append is LoadState.Error)
                showToast(
                    (states.append as LoadState.Error).error.message ?: "未知错误,请联系管理员",
                    SNACK_ERROR
                )
            else if (states.refresh is LoadState.Error) {
                showToast(
                    (states.refresh as LoadState.Error).error.message ?: "未知错误,请联系管理员",
                    SNACK_ERROR
                )
            }
        }.collect()
    }

    LaunchedEffect(key1 = lazyState) {
        snapshotFlow { lazyState.firstVisibleItemIndex }.onEach {
            onChangeToTop.invoke(it > 0)
        }.collect()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = lazyState
        ) {
//            if (pageData.itemCount == 0 && AppContext.cachedTopicList.isEmpty() && refreshing)
//                items(5) {
//                    TopicCard(
//                        isLoading = true,
//                        iconUrl = "",
//                        nickName = "",
//                        date = "",
//                        content = "",
//                        tag = listOf()
//                    ) {}
//                }
            if (pageData.itemCount > 0)
                items(pageData) { entity ->
                    entity?.let {
                        TopicCard(
                            iconUrl = it.avatar,
                            nickName = it.nickname,
                            date = it.publishTime,
                            content = it.topicContent,
                            tag = it.topicTag,
                            commentCount = it.commentCount
                        ) { // onClick
                            AppContext.topicDetail += mapOf(Pair(it.topicId, it))
                            navToPostDetail.invoke(it.topicId)
                        }
                    }
                }
            if (refreshing) {
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "正在加载...", fontSize = 16.sp, color = AppTheme.colors.textLightColor)
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
        PullRefreshIndicator(
            refreshing = false,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFAFAFA)
private fun TopicPagePreview() {
    TopicPage(navToPostDetail = {}, onChangeToTop = {})
}