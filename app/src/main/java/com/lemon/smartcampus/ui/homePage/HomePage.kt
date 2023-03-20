package com.lemon.smartcampus.ui.homePage

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.lemon.smartcampus.ui.discoverPage.DiscoverPage
import com.lemon.smartcampus.ui.infoPage.InfoPage
import com.lemon.smartcampus.ui.profilePage.ProfilePage
import com.lemon.smartcampus.ui.widges.BottomNaviBar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

object PageList {
    private var pageList: List<@Composable () -> Unit> = listOf()
    fun getPage(
        index: Int, navController: NavController?
    ): @Composable () -> Unit {
        if (pageList.isEmpty()) pageList = listOf(
            { InfoPage(navController = navController) },
            { DiscoverPage(navController = navController) },
            { ProfilePage(navController = navController) }
        )
        return pageList[index]
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomePage(
    navController: NavController?
) {
    var nowSelect by remember { mutableStateOf(0) }
    val pageState = rememberPagerState(0)

    LaunchedEffect(key1 = nowSelect) {
        pageState.animateScrollToPage(nowSelect)
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
            PageList.getPage(it, navController).invoke()
        }
        BottomNaviBar(nowSelect = nowSelect) {
            nowSelect = it
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun HomepagePreview() {
    HomePage(navController = null)
}