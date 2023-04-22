package com.lemon.smartcampus.ui.discoverPage.tabPage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.lemon.smartcampus.data.globalData.AppContext
import com.lemon.smartcampus.ui.widges.*
import com.lemon.smartcampus.viewModel.topic.TopicViewAction
import com.lemon.smartcampus.viewModel.topic.TopicViewModel
import com.orhanobut.logger.Logger
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ResPage(
    showToast: (String, String) -> Unit = { _, _ -> },
    viewModel: TopicViewModel = viewModel(),
    navToDetail: (String) -> Unit,
    scrollToTop: MutableState<Boolean> = mutableStateOf(false),
    onChangeToTop: (Boolean) -> Unit
) {
    val scope = rememberCoroutineScope()
    val pageData = viewModel.getPage(false).collectAsLazyPagingItems()
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
            .pullRefresh(pullRefreshState)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = lazyState
        ) {
            items(pageData) { data ->
                data?.let {
                    val resName = "${it.resourceName}.${it.resourceLink.split(".").last()}"
                    TopicCard(
                        iconUrl = it.avatar,
                        nickName = it.nickname,
                        date = it.publishTime,
                        content = it.topicContent,
                        tag = it.topicTag,
                        commentCount = it.commentCount,
                        hasRes = true,
                        resCard = {
                            ResCard(
                                resName = it.resourceName,
                                resType = it.resourceType,
                                resLink = it.resourceLink,
                                resSize = it.resourceSize,
                                isDownloaded = !AppContext.downloadedFile[resName].isNullOrBlank(),
                                onDownload = { _ ->
                                    AppContext.topicDetail += mapOf(Pair(it.topicId, it))
                                    navToDetail.invoke(it.topicId)
                                }
                            )
                        }
                    ) {
                        AppContext.topicDetail += mapOf(Pair(it.topicId, it))
                        navToDetail.invoke(it.topicId)
                    }
                }
            }
        }
        PullRefreshIndicator(false, pullRefreshState, Modifier.align(Alignment.TopCenter))
    }
}