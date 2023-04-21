package com.lemon.smartcampus.ui.discoverPage

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.lemon.smartcampus.R
import com.lemon.smartcampus.data.globalData.AppContext
import com.lemon.smartcampus.ui.discoverPage.tabPage.ResPage
import com.lemon.smartcampus.ui.discoverPage.tabPage.TopicPage
import com.lemon.smartcampus.ui.theme.AppTheme
import com.lemon.smartcampus.ui.widges.SearchBar
import com.lemon.smartcampus.ui.widges.TabTitle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

private val tabList = listOf("话题", "资源")

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalPagerApi::class)
@Composable
fun DiscoverPage(
    showToast: (String, String) -> Unit = {_,_ ->},
    navToPostDetail: (String) -> Unit,
    navToPublish: () -> Unit,
    navToAuth: () -> Unit
) {
    var searchKey by remember { mutableStateOf("") }

    var nowSelect by remember { mutableStateOf(0) }
    val pageState = rememberPagerState(0)

    var needToTop by remember { mutableStateOf(false) }
    val scrollToTop = remember { mutableStateOf(false) }

    // 获取当前页面页数
    LaunchedEffect(key1 = pageState) {
        snapshotFlow { pageState.currentPage }.onEach { page ->
            nowSelect = page
        }.collect()
    }

    LaunchedEffect(key1 = nowSelect) {
        pageState.animateScrollToPage(nowSelect)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        backgroundColor = AppTheme.colors.background,
        floatingActionButton = {
            Column(modifier = Modifier.padding(bottom = 50.dp)) {
                if (needToTop) {
                    FloatingActionButton(
                        backgroundColor = AppTheme.colors.schoolBlue,
                        onClick = { scrollToTop.value = true },
                        modifier = Modifier
                            .size(50.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.up),
                            contentDescription = "scroll to top",
                            modifier = Modifier.size(30.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                }
                FloatingActionButton(
                    backgroundColor = AppTheme.colors.card,
                    onClick = {
                        if (!AppContext.profile?.id.isNullOrBlank())
                            navToPublish.invoke()
                        else navToAuth.invoke()
                    },
                    modifier = Modifier.size(50.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.plus),
                        contentDescription = "add",
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp)
        ) {
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
            ) { page ->
                when (page) {
                    0 -> TopicPage(
                        showToast = showToast,
                        navToPostDetail = navToPostDetail,
                        scrollToTop = scrollToTop
                    ) { needToTop = it }
                    1 -> ResPage(
                        showToast = showToast,
                        navToDetail = navToPostDetail,
                        scrollToTop = scrollToTop
                    ) { needToTop = it }
                }
            }
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {

    }

}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFAFAFA)
private fun DiscoverPagePreview() {
    DiscoverPage(navToPostDetail = {}, navToPublish = {}, navToAuth = {})
}