package com.lemon.smartcampus.ui.infoPage

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lemon.smartcampus.R
import com.lemon.smartcampus.ui.widges.*
import com.lemon.smartcampus.viewModel.info.InfoViewAction
import com.lemon.smartcampus.viewModel.info.InfoViewEvent
import com.lemon.smartcampus.viewModel.info.InfoViewModel
import com.zj.mvi.core.observeEvent

@Composable
fun InfoPage(
    showToast: (String, String) -> Unit = { _, _ -> },
    viewModel: InfoViewModel = viewModel(),
    navToList: (Int) -> Unit = {},
    navToInfoDetail: (String) -> Unit = {},
    navToCharacter: () -> Unit = {},
    navToIntro: () -> Unit = {},
    navToCalendarPage: () -> Unit = {}
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val state by viewModel.viewStates.collectAsState()
    var searchKey by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()
    LaunchedEffect(key1 = Unit) {
        viewModel.dispatch(InfoViewAction.Request)

        viewModel.viewEvents.observeEvent(lifecycleOwner) { events ->
            when (events) {
                is InfoViewEvent.ShowToast -> showToast(events.msg, SNACK_ERROR)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp)
    ) {
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
                HomePageIBtn(icon = R.drawable.building, text = "学校概况") { navToIntro() }
                HomePageIBtn(icon = R.drawable.medal, text = "广外人物") { navToCharacter() }
                HomePageIBtn(icon = R.drawable.calendar, text = "最新校历") { navToCalendarPage() }
                HomePageIBtn(icon = R.drawable.offical, text = "学校官网") {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.gdufs.edu.cn/"))
                    context.startActivity(intent)
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
            HomePageTitle(title = "广外新闻", titleEn = "News") {
                navToList.invoke(InfoType.NEWS)
            }
            NewsCardList(data = state.newsList) {
                if (state.newsList.isNotEmpty())
                    navToInfoDetail.invoke(state.newsList[it].id)
            }
            Spacer(modifier = Modifier.height(12.dp))
            HomePageTitle(title = "学术研究", titleEn = "Academic") {
                navToList.invoke(InfoType.ACADEMIC)
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
                        if (state.academicList.isNotEmpty())
                            navToInfoDetail.invoke(state.academicList[it + 1].id)
                    },
                    onClickTop = {
                        if (state.academicList.isNotEmpty())
                            navToInfoDetail.invoke(state.academicList[0].id)
                    }
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun InfoPagePreview() {
    InfoPage()
}