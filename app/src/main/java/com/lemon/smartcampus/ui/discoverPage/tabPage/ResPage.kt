package com.lemon.smartcampus.ui.discoverPage.tabPage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ScaffoldState
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.lemon.smartcampus.data.globalData.AppContext
import com.lemon.smartcampus.ui.widges.ResCard
import com.lemon.smartcampus.ui.widges.ResType
import com.lemon.smartcampus.ui.widges.TopicCard
import com.lemon.smartcampus.utils.DETAILS_PAGE
import com.lemon.smartcampus.viewModel.topic.TopicViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ResPage(
    navController: NavController?,
    scaffoldState: ScaffoldState? = null,
    viewModel: TopicViewModel = viewModel(),
    needToTop: Boolean = false,
    onScrollTop: () -> Unit
) {
    val pageData = viewModel.getPage(false).collectAsLazyPagingItems()
    val isRefresh by remember{ mutableStateOf(false)}

    val pullRefreshState = rememberPullRefreshState(isRefresh, { pageData.refresh() })
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (pageData.itemCount == 0)
                items(6) {
                    TopicCard(
                        isLoading = true,
                        iconUrl = "",
                        nickName = "",
                        date = "",
                        content = "",
                        tag = listOf(),
                        hasRes = true,
                        resCard = {
                            ResCard(
                                resName = "",
                                resType = ResType.UNKNOWN,
                                resLink = "",
                                resSize = 0f,
                                onDownload = {}
                            )
                        }
                    ) {}
                }
            items(pageData) { data ->
                data?.let {
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
                                onDownload = {}
                            )
                        }
                    ) { // onClick
                        AppContext.topicDetail += mapOf(Pair(it.topicId, it))
                        navController?.navigate("$DETAILS_PAGE/${it.topicId}")
                    }
                }
            }
        }
        PullRefreshIndicator(isRefresh, pullRefreshState, Modifier.align(Alignment.TopCenter))
    }
}