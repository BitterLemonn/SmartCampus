package com.lemon.smartcampus.ui.discoverPage.tabPage

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ScaffoldState
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.lemon.smartcampus.data.database.entities.CommentEntity
import com.lemon.smartcampus.data.database.entities.TopicEntity
import com.lemon.smartcampus.data.globalData.AppContext
import com.lemon.smartcampus.ui.widges.*
import com.lemon.smartcampus.viewModel.topic.DetailViewAction
import com.lemon.smartcampus.viewModel.topic.DetailViewEvent
import com.lemon.smartcampus.viewModel.topic.DetailViewModel
import com.orhanobut.logger.Logger
import com.zj.mvi.core.observeEvent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TopicDetailPage(
    navController: NavController?,
    scaffoldState: ScaffoldState? = null,
    viewModel: DetailViewModel = viewModel(),
    id: String
) {
    var isTouchOutSide by remember { mutableStateOf(false) }
    var isShow by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }

    val host = AppContext.topicDetail[id] ?: TopicEntity.getEmpty()
    val state by viewModel.viewStates.collectAsState()
    val lazyState = rememberLazyListState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()

    val pullState = rememberPullRefreshState(
        refreshing = loading,
        onRefresh = { viewModel.dispatch(DetailViewAction.Refresh) }
    )

    LaunchedEffect(key1 = Unit) {
        viewModel.dispatch(DetailViewAction.UpdateTopicId(host.topicId))
        viewModel.dispatch(DetailViewAction.Refresh)

        viewModel.viewEvents.observeEvent(lifecycleOwner) {
            when (it) {
                is DetailViewEvent.ShowLoadingDialog -> loading = true
                is DetailViewEvent.DismissLoadingDialog -> loading = false
                is DetailViewEvent.ShowToast -> {
                    scaffoldState?.let { scaffoldState ->
                        if (it.success)
                            popupSnackBar(scope, scaffoldState, SNACK_SUCCESS, it.msg)
                        else
                            popupSnackBar(scope, scaffoldState, SNACK_ERROR, it.msg)
                    }
                }
            }
        }
    }

    LaunchedEffect(key1 = state.commentList) {
        Logger.d("change")
    }

    LaunchedEffect(key1 = lazyState) {
        snapshotFlow { lazyState.firstVisibleItemIndex }.onEach {
            Logger.d("nowSeen:$it")
            if (state.commentList.size - it < 10) {
                if (state.commentList.isEmpty()) viewModel.dispatch(DetailViewAction.Refresh)
                else viewModel.dispatch(DetailViewAction.GetNextPage)
            }
            viewModel.dispatch(DetailViewAction.UpdateAtTop(it <= 1))
        }.collect()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = MutableInteractionSource(),
                onClick = {
                    isTouchOutSide = true
                    isShow = false
                })
    ) {
        ToolBarMore(onBack = { navController?.popBackStack() }, onMore = {
            if (AppContext.profile?.id == host.userId) isShow = true
        })

        Box(modifier = Modifier
            .pullRefresh(pullState)
            .weight(1f)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 20.dp),
                state = lazyState
            ) {
                item { CommentHostCard(topic = host) }
                items(state.commentList) {
                    CommentSubCard(comment = it)
                }
            }
            PullRefreshIndicator(loading, pullState, Modifier.align(Alignment.TopCenter))
        }
        ChatBox(
            key = state.chat,
            isTouchOutside = isTouchOutSide,
            onChange = { viewModel.dispatch(DetailViewAction.UpdateChat(it)) },
            onSend = { viewModel.dispatch(DetailViewAction.SendChat) },
            onTouch = { isTouchOutSide = false })
    }

    if (host.userId == AppContext.profile?.id && host.userId.isNotBlank())
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
            MoreActionCard(listOf(ActionPair("删除话题") {
                // TODO 删除
            }), isShow)
        }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFAFAFA)
private fun ResDetailPagePreview() {
    TopicDetailPage(navController = null, id = "")
}