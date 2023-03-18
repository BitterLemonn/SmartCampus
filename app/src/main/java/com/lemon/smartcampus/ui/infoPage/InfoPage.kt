package com.lemon.smartcampus.ui.infoPage

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.lemon.smartcampus.R
import com.lemon.smartcampus.data.entities.AcademicEntity
import com.lemon.smartcampus.data.entities.NewsEntity
import com.lemon.smartcampus.ui.widges.*

@Composable
fun InfoPage(
    navController: NavController?
) {
    val scrollState = rememberScrollState()
    val newsList = remember { mutableStateListOf<NewsEntity>() }
    val academicList = remember { mutableStateListOf<AcademicEntity>() }

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

            }
            HomePageIBtn(icon = R.drawable.medal, text = "广外人物") {

            }
            HomePageIBtn(icon = R.drawable.calendar, text = "最新校历") {

            }
            HomePageIBtn(icon = R.drawable.offical, text = "学校官网") {

            }
        }
        Spacer(modifier = Modifier.height(18.dp))
        HomePageTitle(title = "广外新闻", titleEn = "News") {

        }
        NewsCardList(data = newsList) {

        }
        Spacer(modifier = Modifier.height(12.dp))
        HomePageTitle(title = "学术研究", titleEn = "Academic") {

        }
        Spacer(modifier = Modifier.height(12.dp))
        Box(modifier = Modifier.weight(1f)) {
            AcademicList(
                onLoad = academicList.isEmpty(),
                academicList = academicList,
                onClick = {},
                onClickTop = {}
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun InfoPagePreview() {
    InfoPage(navController = null)
}