package com.lemon.smartcampus.ui.course

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.lemon.smartcampus.R
import com.lemon.smartcampus.ui.theme.AppTheme
import com.lemon.smartcampus.ui.widges.ColoredTitleBar
import com.lemon.smartcampus.ui.widges.CourseCard
import com.lemon.smartcampus.ui.widges.SNACK_WARN
import com.lemon.smartcampus.ui.widges.popupSnackBar
import com.lemon.smartcampus.utils.COURSE_EDIT_PAGE
import com.lemon.smartcampus.utils.indexToChar
import com.lemon.smartcampus.viewModel.course.CourseViewAction
import com.lemon.smartcampus.viewModel.course.CourseViewEvent
import com.lemon.smartcampus.viewModel.course.CourseViewModel
import com.sd.lib.compose.wheel_picker.FVerticalWheelPicker
import com.sd.lib.compose.wheel_picker.rememberFWheelPickerState
import com.zj.mvi.core.observeEvent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CoursePage(
    navController: NavController?,
    scaffoldState: ScaffoldState?,
    viewModel: CourseViewModel = viewModel()
) {
    val state by viewModel.viewStates.collectAsState()
    val scope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current

    val weekState = rememberFWheelPickerState(state.week)
    val dState = rememberFWheelPickerState()

    var expand by remember { mutableStateOf(false) }
    var isChange by remember { mutableStateOf(false) }
    var reCompose by remember { mutableStateOf(false) }

    val reComposeAlpha by animateFloatAsState(targetValue = if (reCompose) 0.99f else 1f)
    val animateHeight by animateDpAsState(targetValue = if (expand) 100.dp else 50.dp)
    LaunchedEffect(key1 = Unit) {
        // TODO 开发警告
        scaffoldState?.let {
            popupSnackBar(
                scope,
                scaffoldState,
                SNACK_WARN,
                "!!!注意!!!该功能正在开发或者测试当中"
            )
        }
        viewModel.dispatch(CourseViewAction.GetOneDayCourse)
        viewModel.viewEvents.observeEvent(lifecycleOwner) { events ->
            when (events) {
                is CourseViewEvent.Recompose -> reCompose = true
            }
        }
    }
    LaunchedEffect(key1 = weekState) {
        snapshotFlow { weekState.currentIndex }.collectLatest {
            viewModel.dispatch(CourseViewAction.UpdateWeek(if (it <= 0) 1 else it + 1))
            isChange = true
        }
    }
    LaunchedEffect(key1 = dState) {
        snapshotFlow { dState.currentIndex }.collectLatest {
            viewModel.dispatch(CourseViewAction.UpdateDInWeek(indexToChar(if (it < 0) 0 else it)))
            isChange = true
        }
    }
    LaunchedEffect(key1 = expand) {
        if (!expand && isChange) {
            viewModel.dispatch(CourseViewAction.GetOneDayCourse)
        }
    }
    LaunchedEffect(key1 = reCompose) {
        if (reCompose) reCompose = false
    }

    Scaffold(modifier = Modifier.alpha(reComposeAlpha), floatingActionButton = {
        FloatingActionButton(
            onClick = { navController?.navigate(COURSE_EDIT_PAGE) { launchSingleTop } },
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
                            text = "第${state.week}周 星期${state.dInWeek}",
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
                items(state.oneDayCourse) {
                    CourseCard(courseEntity = it) {
                        navController?.navigate("$COURSE_EDIT_PAGE?courseID=${it.id}")
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFAFAFA)
fun CoursePagePreview() {
    CoursePage(null, null)
}