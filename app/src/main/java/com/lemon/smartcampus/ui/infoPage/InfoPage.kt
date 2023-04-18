package com.lemon.smartcampus.ui.infoPage

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.lemon.smartcampus.R
import com.lemon.smartcampus.ui.widges.*
import com.lemon.smartcampus.utils.INFO_DETAIL
import com.lemon.smartcampus.utils.INFO_LIST
import com.lemon.smartcampus.viewModel.info.InfoViewAction
import com.lemon.smartcampus.viewModel.info.InfoViewEvent
import com.lemon.smartcampus.viewModel.info.InfoViewModel
import com.zj.mvi.core.observeEvent

@Composable
fun InfoPage(
    navController: NavController?,
    scaffoldState: ScaffoldState?,
    viewModel: InfoViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current
    val state by viewModel.viewStates.collectAsState()
    var searchKey by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()
    LaunchedEffect(key1 = Unit) {
        viewModel.dispatch(InfoViewAction.Request)

        viewModel.viewEvents.observeEvent(lifecycleOwner) { events ->
            when (events) {
                is InfoViewEvent.ShowToast -> scaffoldState?.let {
                    popupSnackBar(scope, scaffoldState, SNACK_ERROR, events.msg)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp)
    ) {
        SearchBar(
            key = searchKey,
            onKeyChange = {
                // TODO 主页搜索
            },
            onSearch = {
                // TODO 主页搜索
            },
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                HomePageIBtn(icon = R.drawable.building, text = "学校概况") {
                    // TODO 开发警告
                    scaffoldState?.let {
                        popupSnackBar(
                            scope, scaffoldState, SNACK_WARN,
                            "!!!注意!!!该功能正在开发或者测试当中"
                        )
                    }
                }
                HomePageIBtn(icon = R.drawable.medal, text = "广外人物") {
                    // TODO 开发警告
                    scaffoldState?.let {
                        popupSnackBar(
                            scope, scaffoldState, SNACK_WARN,
                            "!!!注意!!!该功能正在开发或者测试当中"
                        )
                    }
                }
                HomePageIBtn(icon = R.drawable.calendar, text = "最新校历") {
                    // TODO 开发警告
                    scaffoldState?.let {
                        popupSnackBar(
                            scope, scaffoldState, SNACK_WARN,
                            "!!!注意!!!该功能正在开发或者测试当中"
                        )
                    }
                }
                HomePageIBtn(icon = R.drawable.offical, text = "学校官网") {
                    // TODO 开发警告
                    scaffoldState?.let {
                        popupSnackBar(
                            scope, scaffoldState, SNACK_WARN,
                            "!!!注意!!!该功能正在开发或者测试当中"
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
            HomePageTitle(title = "广外新闻", titleEn = "News") {
                navController?.navigate("$INFO_LIST/${InfoType.NEWS}"){
                    launchSingleTop
                }
            }
            NewsCardList(data = state.newsList) {
                navController?.navigate("$INFO_DETAIL/${state.newsList[it].id}"){
                    launchSingleTop
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            HomePageTitle(title = "学术研究", titleEn = "Academic") {
                navController?.navigate("$INFO_LIST/${InfoType.ACADEMIC}"){
                    launchSingleTop
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Box(modifier = Modifier.weight(1f)) {
                AcademicList(
                    onLoad = state.academicList.isEmpty(),
                    titleTop = state.academicList.getOrNull(0)?.informationTitle ?: "",
                    contentTop = state.academicList.getOrNull(0)?.informationContent ?: "",
                    academicList = if (state.academicList.size > 1)
                        state.academicList.subList(1, state.academicList.size) else listOf(),
                    onClick = {
                        navController?.navigate("$INFO_DETAIL/${state.academicList[it + 1].id}")
                    },
                    onClickTop = {
                        navController?.navigate("$INFO_DETAIL/${state.academicList[0].id}")
                    }
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun InfoPagePreview() {
    InfoPage(navController = null, null)
}