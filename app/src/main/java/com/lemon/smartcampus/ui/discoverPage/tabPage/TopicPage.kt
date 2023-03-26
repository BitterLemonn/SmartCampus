package com.lemon.smartcampus.ui.discoverPage.tabPage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ScaffoldState
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.lemon.smartcampus.data.globalData.AppContext
import com.lemon.smartcampus.ui.widges.SNACK_ERROR
import com.lemon.smartcampus.ui.widges.TopicCard
import com.lemon.smartcampus.ui.widges.popupSnackBar
import com.lemon.smartcampus.utils.DETAILS_PAGE
import com.lemon.smartcampus.viewModel.topic.TopicViewModel
import com.orhanobut.logger.Logger
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TopicPage(
    navController: NavController?,
    scaffoldState: ScaffoldState? = null,
    viewModel: TopicViewModel = viewModel()
) {

    val scope = rememberCoroutineScope()
    val pageData = viewModel.getPage(true).collectAsLazyPagingItems()
    var refreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(refreshing, { pageData.refresh() })
    val lazyState = rememberLazyListState()

    LaunchedEffect(key1 = Unit) {
        snapshotFlow { pageData.loadState }.collectLatest { states ->
            Logger.d("state: $states")
            when (states.refresh) {
                is LoadState.Loading -> {}
                is LoadState.Error -> {
                    val error = (states.refresh as LoadState.Error).error
                    scaffoldState?.let {
                        popupSnackBar(
                            scope, scaffoldState, SNACK_ERROR,
                            error.message ?: "未知错误,请联系管理员"
                        )
                    }
                }
                is LoadState.NotLoading -> {}
            }
            when (states.append) {
                is LoadState.Error -> {
                    val error = (states.refresh as LoadState.Error).error
                    scaffoldState?.let {
                        popupSnackBar(
                            scope, scaffoldState, SNACK_ERROR,
                            error.message ?: "未知错误,请联系管理员"
                        )
                    }
                }
                else -> {}
            }
        }
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
            if (pageData.itemCount == 0)
                items(5) {
                    TopicCard(
                        isLoading = true,
                        iconUrl = "",
                        nickName = "",
                        date = "",
                        content = "",
                        tag = listOf()
                    ) {}
                }
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
                        navController?.navigate("$DETAILS_PAGE/${it.topicId}")
                    }
                }
            }
        }
        PullRefreshIndicator(refreshing, pullRefreshState, Modifier.align(Alignment.TopCenter))
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFAFAFA)
private fun TopicPagePreview() {
    TopicPage(navController = null)
}