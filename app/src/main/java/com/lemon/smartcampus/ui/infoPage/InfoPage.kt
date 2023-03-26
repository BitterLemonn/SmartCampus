package com.lemon.smartcampus.ui.infoPage

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.lemon.smartcampus.R
import com.lemon.smartcampus.data.database.entities.AcademicEntity
import com.lemon.smartcampus.data.database.entities.NewsEntity
import com.lemon.smartcampus.ui.widges.*

@Composable
fun InfoPage(
    navController: NavController?,
    scaffoldState: ScaffoldState?
) {
    val scope = rememberCoroutineScope()
    var searchKey by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()
    val newsList = remember { mutableStateListOf<NewsEntity>() }
    val academicList = remember { mutableStateListOf<AcademicEntity>() }

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
                // TODO 开发警告
                scaffoldState?.let {
                    popupSnackBar(
                        scope, scaffoldState, SNACK_WARN,
                        "!!!注意!!!该功能正在开发或者测试当中"
                    )
                }
            }
            NewsCardList(data = newsList) {
                // TODO 开发警告
                scaffoldState?.let {
                    popupSnackBar(
                        scope, scaffoldState, SNACK_WARN,
                        "!!!注意!!!该功能正在开发或者测试当中"
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            HomePageTitle(title = "学术研究", titleEn = "Academic") {
                // TODO 开发警告
                scaffoldState?.let {
                    popupSnackBar(
                        scope, scaffoldState, SNACK_WARN,
                        "!!!注意!!!该功能正在开发或者测试当中"
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Box(modifier = Modifier.weight(1f)) {
                AcademicList(
                    onLoad = academicList.isEmpty(),
                    academicList = academicList,
                    onClick = {
                        // TODO 开发警告
                        scaffoldState?.let {
                            popupSnackBar(
                                scope, scaffoldState, SNACK_WARN,
                                "!!!注意!!!该功能正在开发或者测试当中"
                            )
                        }
                    },
                    onClickTop = {
                        // TODO 开发警告
                        scaffoldState?.let {
                            popupSnackBar(
                                scope, scaffoldState, SNACK_WARN,
                                "!!!注意!!!该功能正在开发或者测试当中"
                            )
                        }
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