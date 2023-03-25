package com.lemon.smartcampus.ui.discoverPage

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.lemon.smartcampus.R
import com.lemon.smartcampus.data.globalData.AppContext
import com.lemon.smartcampus.ui.discoverPage.tabPage.ResPage
import com.lemon.smartcampus.ui.discoverPage.tabPage.TopicPage
import com.lemon.smartcampus.ui.widges.SearchBar
import com.lemon.smartcampus.ui.widges.TabTitle
import com.lemon.smartcampus.utils.AUTH_PAGE
import com.lemon.smartcampus.utils.PUBLISH_PAGE
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

val tabList = listOf("话题", "资源")

object TabPage {
    private var pageList = listOf<@Composable () -> Unit>()
    fun getPage(
        index: Int,
        navController: NavController?,
        scaffoldState: ScaffoldState? = null,
        needToTop: Boolean = false,
        onScrollTop: () -> Unit
    ): @Composable () -> Unit {
        if (pageList.isEmpty()) {
            pageList = listOf(
                { TopicPage(navController = navController, scaffoldState = scaffoldState) },
                {
                    ResPage(
                        navController = navController,
                        scaffoldState = scaffoldState,
                        needToTop = index == 1 && needToTop
                    ) { onScrollTop.invoke() }
                }
            )
        }
        return pageList[index]
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalPagerApi::class)
@Composable
fun DiscoverPage(
    navController: NavController?,
    scaffoldState: ScaffoldState? = null
) {
    var searchKey by remember { mutableStateOf("") }

    var nowSelect by remember { mutableStateOf(0) }
    val pageState = rememberPagerState(0)

    var needToTop by remember { mutableStateOf(false) }

    // 获取当前页面页数
    LaunchedEffect(key1 = pageState) {
        snapshotFlow { pageState.currentPage }.onEach { page ->
            nowSelect = page
        }.collect()
    }

    LaunchedEffect(key1 = nowSelect) {
        pageState.animateScrollToPage(nowSelect)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp)
    ) {
        SearchBar(
            key = searchKey,
            onKeyChange = {},
            onSearch = {},
            modifier = Modifier.padding(top = 10.dp, start = 20.dp, end = 20.dp)
        )
        TabTitle(tabList = tabList, nowSelect) {
            nowSelect = it
        }
        Spacer(modifier = Modifier.height(7.dp))
        HorizontalPager(
            count = tabList.size,
            modifier = Modifier
                .fillMaxHeight(),
            state = pageState,
            userScrollEnabled = false
        ) {
            TabPage.getPage(it, navController, scaffoldState, needToTop) { needToTop = false }
                .invoke()
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(
            backgroundColor = if (!isSystemInDarkTheme()) Color.White else Color(0xFF2A2A2A),
            onClick = {
                if (!AppContext.profile?.id.isNullOrBlank())
                    navController?.navigate(PUBLISH_PAGE) {
                        launchSingleTop
                        restoreState
                    }
                else navController?.navigate(AUTH_PAGE) {
                    launchSingleTop
                    restoreState
                }
            },
            modifier = Modifier
                .absolutePadding(
                    left = maxWidth * 0.8f,
                    top = maxHeight * 0.8f
                )
                .size(50.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.plus),
                contentDescription = "add",
                modifier = Modifier.size(30.dp)
            )
        }
    }

}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFAFAFA)
private fun DiscoverPagePreview() {
    DiscoverPage(navController = null)
}