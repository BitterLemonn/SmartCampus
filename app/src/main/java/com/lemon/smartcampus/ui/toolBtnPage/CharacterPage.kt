package com.lemon.smartcampus.ui.toolBtnPage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.lemon.smartcampus.ui.toolBtnPage.characterPage.CharacterListPage
import com.lemon.smartcampus.ui.toolBtnPage.characterPage.ScholarListPage
import com.lemon.smartcampus.ui.widges.ColoredTitleBar
import com.lemon.smartcampus.ui.widges.TabTitle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CharacterPage(
    showToast: (String, String) -> Unit,
    onBack: () -> Unit,
    navToCharacterDetail: (String, String, String) -> Unit
) {
    val listPage = listOf(
        Pair<String, @Composable () -> Unit>("杰出学者") {
            ScholarListPage(showToast = showToast, navToCharacterDetail = navToCharacterDetail)
        },
        Pair<String, @Composable () -> Unit>("杰出校友") {
            CharacterListPage(showToast = showToast, navToCharacterDetail = navToCharacterDetail)
        },
    )
    val pagerState = rememberPagerState()
    var nowSelect by remember { mutableStateOf(0) }

    LaunchedEffect(key1 = pagerState) {
        snapshotFlow { pagerState.currentPage }.onEach { nowSelect = it }.collect()
    }
    LaunchedEffect(key1 = nowSelect) {
        pagerState.animateScrollToPage(nowSelect)
    }
    Column(Modifier.fillMaxSize()) {
        ColoredTitleBar(color = Color.Transparent, text = "") { onBack() }
        Spacer(modifier = Modifier.height(10.dp))
        TabTitle(
            tabList = listPage.map { it.first },
            onClick = { nowSelect = it },
            nowSelect = nowSelect
        )
        HorizontalPager(count = 2, state = pagerState) {
            listPage[it].second.invoke()
        }
    }
}