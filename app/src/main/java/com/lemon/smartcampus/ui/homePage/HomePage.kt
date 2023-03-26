package com.lemon.smartcampus.ui.homePage

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.lemon.smartcampus.ui.discoverPage.DiscoverPage
import com.lemon.smartcampus.ui.infoPage.InfoPage
import com.lemon.smartcampus.ui.profilePage.ProfilePage
import com.lemon.smartcampus.ui.theme.BackgroundDay
import com.lemon.smartcampus.ui.widges.BottomNaviBar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

private object PageList {
    private var pageList: List<@Composable () -> Unit> = listOf()
    fun getPage(
        index: Int, navController: NavController?, scaffoldState: ScaffoldState?
    ): @Composable () -> Unit {
        if (pageList.isEmpty()) pageList = listOf(
            { InfoPage(navController = navController, scaffoldState = scaffoldState) },
            { DiscoverPage(navController = navController, scaffoldState = scaffoldState) },
            { ProfilePage(navController = navController, scaffoldState = scaffoldState) }
        )
        return pageList[index]
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomePage(
    navController: NavController?,
    scaffoldState: ScaffoldState?
) {
    var nowSelect by remember { mutableStateOf(0) }
    var showBar by remember { mutableStateOf(true) }
    val pageState = rememberPagerState(0)

    val uiController = rememberSystemUiController()
    val isNight = isSystemInDarkTheme()

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
                PageList.getPage(it, navController, scaffoldState).invoke()
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
    HomePage(navController = null, scaffoldState = null)
}