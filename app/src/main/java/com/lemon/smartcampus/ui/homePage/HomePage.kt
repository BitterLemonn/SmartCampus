package com.lemon.smartcampus.ui.homePage

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.lemon.smartcampus.ui.discoverPage.DiscoverPage
import com.lemon.smartcampus.ui.infoPage.InfoPage
import com.lemon.smartcampus.ui.profilePage.ProfilePage
import com.lemon.smartcampus.ui.theme.AppTheme
import com.lemon.smartcampus.ui.theme.BackgroundDay
import com.lemon.smartcampus.ui.widges.BottomNaviBar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

object PageList {
    private var pageList: List<@Composable () -> Unit> = listOf()
    fun getPage(
        index: Int, navController: NavController?, scaffoldState: ScaffoldState?
    ): @Composable () -> Unit {
        if (pageList.isEmpty()) pageList = listOf(
            { InfoPage(navController = navController) },
            { DiscoverPage(navController = navController) },
            { ProfilePage(navController = navController, scaffoldState) }
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
    val pageState = rememberPagerState(0)

    val uiController = rememberSystemUiController()
    val isNight = isSystemInDarkTheme()

    LaunchedEffect(key1 = nowSelect) {
        pageState.animateScrollToPage(nowSelect)
        if (nowSelect == 2)
            uiController.setStatusBarColor(Color.Transparent)
        else
            uiController.setStatusBarColor(if (!isNight) BackgroundDay else Color.Black)
    }

    LaunchedEffect(key1 = pageState) {
        snapshotFlow { pageState.currentPage }.onEach { page ->
            nowSelect = page
        }.collect()
    }

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
        BottomNaviBar(nowSelect = nowSelect) {
            nowSelect = it
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun HomepagePreview() {
    HomePage(navController = null, scaffoldState = null)
}