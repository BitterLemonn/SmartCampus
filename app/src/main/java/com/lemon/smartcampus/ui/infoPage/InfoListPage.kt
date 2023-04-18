package com.lemon.smartcampus.ui.infoPage

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.lemon.smartcampus.ui.theme.AppTheme
import com.lemon.smartcampus.ui.widges.*
import com.lemon.smartcampus.utils.COUNT_PER_PAGE
import com.lemon.smartcampus.utils.INFO_DETAIL
import com.lemon.smartcampus.viewModel.info.InfoListViewAction
import com.lemon.smartcampus.viewModel.info.InfoListViewEvent
import com.lemon.smartcampus.viewModel.info.InfoListViewModel
import com.zj.mvi.core.observeEvent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

object InfoType {
    const val NEWS = 0
    const val ACADEMIC = 1
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun InfoListPage(
    navController: NavController?,
    scaffoldState: ScaffoldState?,
    type: Int,
    viewModel: InfoListViewModel = viewModel()
) {
    val pullState = rememberPullRefreshState(
        refreshing = false,
        onRefresh = { viewModel.dispatch(InfoListViewAction.Refresh) })
    val lazyState = rememberLazyListState()
    var isShowLoading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val state by viewModel.viewStates.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(key1 = Unit) {
        viewModel.dispatch(InfoListViewAction.UpdateType(type))
        viewModel.dispatch(InfoListViewAction.Refresh)
        viewModel.viewEvents.observeEvent(lifecycleOwner) { events ->
            when (events) {
                is InfoListViewEvent.ShowLoadingDialog -> isShowLoading = true
                is InfoListViewEvent.DismissLoadingDialog -> isShowLoading = false
                is InfoListViewEvent.ShowToast -> scaffoldState?.let {
                    popupSnackBar(
                        scope = scope,
                        scaffoldState = scaffoldState,
                        if (events.success) SNACK_SUCCESS else SNACK_ERROR,
                        events.msg
                    )
                }
            }
        }
    }

    LaunchedEffect(key1 = lazyState) {
        snapshotFlow { lazyState.firstVisibleItemIndex }.onEach {
            val list = if (type == InfoType.NEWS) state.newsList else state.academicList
            if (list.size - COUNT_PER_PAGE * 2 <= it && it != 0)
                viewModel.dispatch(InfoListViewAction.GetNextPage)
        }.collect()
    }

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        ColoredTitleBar(
            color = Color.Transparent,
            text = if (type == InfoType.NEWS) "新闻列表" else "学术列表"
        ) { navController?.popBackStack() }
    }, backgroundColor = AppTheme.colors.background) {
        Box(
            modifier = Modifier
                .pullRefresh(pullState)
                .padding(it)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 20.dp),
                state = lazyState
            ) {
                if (type == InfoType.NEWS)
                    items(state.newsList) { entity ->
                        NewsCard(
                            imageUrl = entity.informationCover,
                            date = entity.publishDate,
                            title = entity.informationTitle
                        ) { navController?.navigate("$INFO_DETAIL/${entity.id}") }
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                else
                    items(state.academicList) { entity ->
                        AcademicCardCommon(
                            title = entity.informationTitle,
                            date = entity.publishDate
                        ) { navController?.navigate("$INFO_DETAIL/${entity.id}") }
                        Spacer(modifier = Modifier.height(20.dp))
                    }
            }
            PullRefreshIndicator(state.loading, pullState, Modifier.align(Alignment.TopCenter))
        }
    }

    if (isShowLoading)
        WarpLoadingDialog()

}