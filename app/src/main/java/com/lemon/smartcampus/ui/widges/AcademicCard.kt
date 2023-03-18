package com.lemon.smartcampus.ui.widges

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lemon.smartcampus.data.entities.AcademicEntity
import com.lemon.smartcampus.ui.theme.HintGrayDay
import com.lemon.smartcampus.ui.theme.TextLightDay

@Composable
fun AcademicCardTop(
    onLoad: Boolean = false,
    title: String = "",
    content: String = "",
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick.invoke() }
    ) {
        if (!onLoad) {
            Text(text = title, fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(modifier = Modifier.height(7.dp))
            Text(
                text = content,
                fontSize = 12.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = TextLightDay
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(22.dp)
                    .background(Color(0xFFC4C4C4)),
            )
            Spacer(modifier = Modifier.height(7.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .background(Color(0xFFEAEAEA)),
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(16.dp)
                    .background(Color(0xFFEAEAEA)),
            )
        }
    }
}

@Composable
fun AcademicCardCommon(
    onLoad: Boolean = false,
    title: String = "",
    date: String = "1980-01-01",
    onClick: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .clickable { onClick.invoke() }
    ) {
        Spacer(modifier = Modifier.height(7.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (!onLoad) {
                Text(
                    text = "·  $title",
                    fontSize = 14.sp,
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth(0.6f),
                    overflow = TextOverflow.Ellipsis
                )
                Text(text = date, fontSize = 14.sp, color = TextLightDay)
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(16.dp)
                        .background(Color(0xFFC4C4C4)),
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(16.dp)
                        .background(Color(0xFFEAEAEA)),
                )
            }
        }
    }

}

@Composable
fun AcademicList(
    onLoad: Boolean,
    titleTop: String = "",
    contentTop: String = "",
    academicList: List<AcademicEntity>,
    onClick: (Int) -> Unit,
    onClickTop: () -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
        item {
            AcademicCardTop(
                onLoad = onLoad,
                title = titleTop,
                content = contentTop,
                onClick = onClickTop
            )
        }
        item {
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                color = HintGrayDay
            )
        }
        if (!onLoad)
            for (item in academicList){
                item{
                    AcademicCardCommon(title = item.title, date = item.date) {
                        onClick.invoke(academicList.indexOf(item))
                    }
                }
                item{
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        color = HintGrayDay
                    )
                }
            }
        else
            for (x in 0..3) {
                item {
                    AcademicCardCommon(onLoad = true) {}
                }
                item{
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        color = HintGrayDay
                    )
                }
            }
    }
}

@Composable
@Preview
fun AcademicCardPreview() {
//    AcademicCardTop(
//        onLoad = false,
//        title = "标题标题标题标题标题",
//        content = "这是简介内容这是简介内容这是简介内容这是简介内容这是简介内容这是简介内容这是简介内容这是简介内容"
//    )
//    AcademicCardCommon(
//        onLoad = true,
//        title = "标题标题标题标题标题"
//    ){}
    AcademicList(onLoad = true, academicList = listOf(), onClick = {}, onClickTop = {})
}