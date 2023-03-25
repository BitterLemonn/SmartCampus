package com.lemon.smartcampus.ui.course

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lemon.smartcampus.R
import com.lemon.smartcampus.ui.theme.AppTheme
import com.lemon.smartcampus.ui.widges.ColoredTitleBar
import com.sd.lib.compose.wheel_picker.FVerticalWheelPicker
import com.sd.lib.compose.wheel_picker.rememberFWheelPickerState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun CoursePage(
    navController: NavController?
) {
    var expand by remember { mutableStateOf(false) }
    val animateHeight by animateDpAsState(targetValue = if (expand) 100.dp else 50.dp)
    var week by remember { mutableStateOf(1) }
    var dInWeek by remember { mutableStateOf("一") }

    val weekState = rememberFWheelPickerState()
    val dState = rememberFWheelPickerState()

    LaunchedEffect(key1 = weekState) {
        snapshotFlow { weekState.currentIndex }.onEach { week = if (it <= 0) 1 else it + 1 }
            .collect()
    }
    LaunchedEffect(key1 = dState) {
        snapshotFlow { dState.currentIndex }.onEach {
            dInWeek = indexToChar(if (it < 0) 0 else it)
        }.collect()
    }

    Scaffold(floatingActionButton = {
        FloatingActionButton(
            onClick = { /*TODO 添加*/ },
            modifier = Modifier.padding(bottom = 60.dp),
            shape = CircleShape,
            backgroundColor = AppTheme.colors.card,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 10.dp, pressedElevation = 15.dp
            )
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.plus),
                    contentDescription = "edit course",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .padding(10.dp)
                )
            }
        }
    }, backgroundColor = AppTheme.colors.background) { p ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(p)
        ) {
            ColoredTitleBar(
                color = if (!isSystemInDarkTheme()) Color(0xFFFFF3D8) else Color(0xFF635D53),
                text = "我的课程"
            ) { navController?.popBackStack() }
            Box(modifier = Modifier.fillMaxWidth()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                        .height(animateHeight),
                    backgroundColor = AppTheme.colors.card,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable(indication = rememberRipple(),
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = { expand = !expand }), contentAlignment = Alignment.Center
                    ) {
                        if (!expand) Text(
                            text = "第${week}周 星期${dInWeek}",
                            fontSize = 16.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp),
                            textAlign = TextAlign.Center,
                            color = AppTheme.colors.textBlackColor
                        )
                        else Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "第",
                                    fontSize = 14.sp,
                                    color = AppTheme.colors.textBlackColor
                                )
                                FVerticalWheelPicker(
                                    count = 20, modifier = Modifier.width(60.dp), state = weekState
                                ) { index ->
                                    Text(
                                        text = "${index + 1}",
                                        fontSize = 14.sp,
                                        color = AppTheme.colors.textBlackColor
                                    )
                                }
                                Text(
                                    text = "周",
                                    fontSize = 14.sp,
                                    color = AppTheme.colors.textBlackColor
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "星期",
                                    fontSize = 14.sp,
                                    color = AppTheme.colors.textBlackColor
                                )
                                FVerticalWheelPicker(
                                    count = 7, modifier = Modifier.width(60.dp), state = dState
                                ) {
                                    Text(
                                        text = indexToChar(it),
                                        fontSize = 14.sp,
                                        color = AppTheme.colors.textBlackColor
                                    )
                                }
                            }

                        }
                    }
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 10.dp)
            ) {

            }
        }
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFAFAFA)
fun CoursePagePreview() {
    CoursePage(null)
}

private fun indexToChar(num: Int): String {
    return when (num) {
        0 -> "一"
        1 -> "二"
        2 -> "三"
        3 -> "四"
        4 -> "五"
        5 -> "六"
        6 -> "日"
        else -> "日"
    }
}