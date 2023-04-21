package com.lemon.smartcampus.ui.homePage

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.lemon.smartcampus.ui.discoverPage.DiscoverPage
import com.lemon.smartcampus.ui.infoPage.InfoPage
import com.lemon.smartcampus.ui.profilePage.ProfilePage
import com.lemon.smartcampus.ui.widges.BottomNaviBar
import com.lemon.smartcampus.utils.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomePage(
    showToast: (String, String) -> Unit = { _, _ -> },
    navToAuth: () -> Unit = {},
    navToPublish: () -> Unit = {},
    navToPostDetail: (String) -> Unit = {},
    navToCourse: () -> Unit = {},
    navToNote: () -> Unit = {},
    navToInfoList: (Int) -> Unit = {},
    navToInfoDetail: (String) -> Unit = {},
    navToIntro: () -> Unit = {},
    navToCalendarPage: () -> Unit = {},
    navToCharacter: () -> Unit = {},
    forceToAuth: () -> Unit = {}
) {
    var nowSelect by remember { mutableStateOf(0) }
    var showBar by remember { mutableStateOf(true) }
    val pageState = rememberPagerState(0)

    val pageList = listOf<@Composable () -> Unit>(
        {
            InfoPage(
                showToast = showToast,
                navToInfoDetail = navToInfoDetail,
                navToList = navToInfoList,
                navToIntro = navToIntro,
                navToCalendarPage = navToCalendarPage,
                navToCharacter = navToCharacter
            )
        },
        {
            DiscoverPage(
                showToast = showToast,
                navToPublish = navToPublish,
                navToAuth = navToAuth,
                navToPostDetail = navToPostDetail
            )
        },
        {
            ProfilePage(
                showToast = showToast,
                navToAuth = navToAuth,
                navToPublish = navToPublish,
                navToPostDetail = navToPostDetail,
                navToCourse = navToCourse,
                navToNote = navToNote,
                forceToAuth = forceToAuth
            )
        }
    )

    LaunchedEffect(key1 = nowSelect) {
        pageState.animateScrollToPage(nowSelect)
    }

    LaunchedEffect(key1 = pageState) {
        snapshotFlow { pageState.currentPage }.onEach { page ->
            nowSelect = page
        }.collect()
    }
    BoxWithConstraints {
        showBar = maxHeight > 600.dp
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            HorizontalPager(
                count = 3,
                modifier = Modifier
                    .weight(1f),
                state = pageState
            ) {
                pageList[it].invoke()
            }
            if (showBar)
                BottomNaviBar(nowSelect = nowSelect) {
                    nowSelect = it
                }
        }
    }

}

@Composable
@Preview(showBackground = true)
private fun HomepagePreview() {
    HomePage()
}