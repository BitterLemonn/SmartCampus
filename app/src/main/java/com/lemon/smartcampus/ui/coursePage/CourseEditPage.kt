package com.lemon.smartcampus.ui.coursePage

import android.content.Intent
import android.provider.AlarmClock
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lemon.smartcampus.R
import com.lemon.smartcampus.data.database.database.GlobalDataBase
import com.lemon.smartcampus.data.globalData.AppContext
import com.lemon.smartcampus.ui.theme.AppTheme
import com.lemon.smartcampus.ui.theme.SchoolBlueDay
import com.lemon.smartcampus.ui.theme.SchoolBlueNight
import com.lemon.smartcampus.ui.widges.*
import com.lemon.smartcampus.utils.SMART_CAMPUS_CN
import com.lemon.smartcampus.utils.indexToChar
import com.lemon.smartcampus.viewModel.course.CourseEditViewAction
import com.lemon.smartcampus.viewModel.course.CourseEditViewEvent
import com.lemon.smartcampus.viewModel.course.CourseEditViewModel
import com.sd.lib.compose.wheel_picker.FVerticalWheelPicker
import com.sd.lib.compose.wheel_picker.rememberFWheelPickerState
import com.zj.mvi.core.observeEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun CourseEditPage(
    scaffoldState: ScaffoldState?,
    courseID: String? = null,
    viewModel: CourseEditViewModel = viewModel(),
    onBack: () -> Unit,
    navToCourseGlobal: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val state by viewModel.viewStates.collectAsState()

    var lessonCount by remember { mutableStateOf(0) }

    var startH by remember { mutableStateOf(7) }
    var startMF by remember { mutableStateOf(0) }
    var startME by remember { mutableStateOf(0) }
    var endH by remember { mutableStateOf(7) }
    var endMF by remember { mutableStateOf(0) }
    var endME by remember { mutableStateOf(0) }

    LaunchedEffect(key1 = Unit) {
        // TODO 开发警告
        scaffoldState?.let {
            popupSnackBar(
                scope, scaffoldState, SNACK_WARN,
                "!!!注意!!!该功能正在开发或者测试当中"
            )
        }
        // 读取编辑ID
        if (!courseID.isNullOrBlank()) {
            scope.launch(Dispatchers.IO) {
                val entity = GlobalDataBase.database.courseDao().get(courseID)
                if (entity != null)
                    viewModel.dispatch(CourseEditViewAction.Init(entity))
            }
        }

        viewModel.viewEvents.observeEvent(lifecycleOwner) { events ->
            when (events) {
                is CourseEditViewEvent.ShowToast ->
                    scaffoldState?.let { popupSnackBar(scope, it, SNACK_ERROR, events.msg) }
                is CourseEditViewEvent.TransIntent -> onBack.invoke()
                is CourseEditViewEvent.SetAlarm -> {
                    val clock = Intent(AlarmClock.ACTION_SET_ALARM)
                    clock.putExtra(AlarmClock.EXTRA_HOUR, events.timeH)
                        .putExtra(AlarmClock.EXTRA_MINUTES, events.timeM)
                        .putExtra(AlarmClock.EXTRA_DAYS, arrayListOf(events.dayInWeek))
                        .putExtra(
                            AlarmClock.EXTRA_MESSAGE,
                            "${SMART_CAMPUS_CN}${state.courseName}${state.location}"
                        )
                        .putExtra(AlarmClock.EXTRA_SKIP_UI, true)
                        .putExtra(AlarmClock.EXTRA_VIBRATE, false)
                    context.startActivity(clock)
                }
            }
        }
    }

    LaunchedEffect(key1 = AppContext.courseGlobal) {
        val setting = AppContext.courseGlobal
        lessonCount = setting.morningClass + setting.noonClass + setting.nightClass
        lessonCount = if (lessonCount > 0) lessonCount else 0
    }

    var expandDInWeek by remember { mutableStateOf(false) }
    val expandWeek = remember { mutableStateOf(false) }
    val expandTime = remember { mutableStateOf(false) }
    var expandAlarm by remember { mutableStateOf(false) }
    var expandClass by remember { mutableStateOf(false) }
    var isShort by remember { mutableStateOf(false) }

    var classIndex by remember { mutableStateOf(1) }

    val weekSState = rememberFWheelPickerState(initialIndex = 0)
    val weekEState = rememberFWheelPickerState(initialIndex = 0)
    LaunchedEffect(key1 = weekEState) {
        snapshotFlow { weekEState.currentIndex }.collectLatest {
            viewModel.dispatch(CourseEditViewAction.UpdateEndWeek(if (it < 0) 1 else it + state.startWeek))
        }
    }
    LaunchedEffect(key1 = weekSState) {
        snapshotFlow { weekSState.currentIndex }.collectLatest {
            viewModel.dispatch(CourseEditViewAction.UpdateStartWeek(if (it < 0) 1 else it + 1))
        }
    }
    val startTimeHState = rememberFWheelPickerState(initialIndex = 7)
    val startTimeMFState = rememberFWheelPickerState(initialIndex = 0)
    val startTimeMEState = rememberFWheelPickerState(initialIndex = 0)
    val endTimeHState = rememberFWheelPickerState(initialIndex = 7)
    val endTimeMFState = rememberFWheelPickerState(initialIndex = 0)
    val endTimeMEState = rememberFWheelPickerState(initialIndex = 0)
    LaunchedEffect(key1 = startTimeHState) {
        snapshotFlow { startTimeHState.currentIndex }.collectLatest {
            startH = if (it < 0) 7 else it
        }
    }
    LaunchedEffect(key1 = startTimeMFState) {
        snapshotFlow { startTimeMFState.currentIndex }.collectLatest {
            startMF = if (it < 0) 0 else it
        }
    }
    LaunchedEffect(key1 = startTimeMEState) {
        snapshotFlow { startTimeMEState.currentIndex }.collectLatest {
            startME = if (it < 0) 0 else it
        }
    }
    LaunchedEffect(key1 = endTimeHState) {
        snapshotFlow { endTimeHState.currentIndex }.collectLatest {
            endH = if (it < 0) 7 else it
        }
    }
    LaunchedEffect(key1 = endTimeMFState) {
        snapshotFlow { endTimeMFState.currentIndex }.collectLatest {
            endMF = if (it < 0) 0 else it
        }
    }
    LaunchedEffect(key1 = endTimeMEState) {
        snapshotFlow { endTimeMEState.currentIndex }.collectLatest {
            endME = if (it < 0) 0 else it
        }
    }
    LaunchedEffect(key1 = expandTime.value) {
        if (!expandTime.value) {
            viewModel.dispatch(CourseEditViewAction.UpdateStartTime("$startH:$startMF$startME"))
            viewModel.dispatch(CourseEditViewAction.UpdateEndTime("$endH:$endMF$endME"))
        }
    }

    val animatedDInWeekRotate by animateFloatAsState(targetValue = if (expandDInWeek) 90f else 270f)
    val animatedWeekRotate by animateFloatAsState(targetValue = if (expandWeek.value) 90f else 270f)
    val animatedAlarmRotate by animateFloatAsState(targetValue = if (expandAlarm) 90f else 270f)
    val animatedClassRotate by animateFloatAsState(targetValue = if (expandClass) 90f else 270f)

    BoxWithConstraints(Modifier.fillMaxSize()) { isShort = maxHeight < 800.dp }
    Column(Modifier.fillMaxSize()) {
        ColoredTitleBar(
            color = if (!isSystemInDarkTheme()) Color(0xFFFFF3D8) else Color(0xFF635D53),
            text = "我的课程",
            onBack = onBack
        )
        // 全局设置按钮
        if (!isShort)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 20.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp),
                elevation = 10.dp,
                backgroundColor = AppTheme.colors.card
            ) {
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(10.dp))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(),
                            onClick = navToCourseGlobal
                        ),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.setting),
                        contentDescription = "global setting",
                        modifier = Modifier
                            .padding(start = maxWidth * 0.3f)
                            .size(30.dp)
                    )
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "全局设置",
                            fontSize = 16.sp,
                            color = AppTheme.colors.textDarkColor
                        )
                    }
                }
            }
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 30.dp),
            shape = RoundedCornerShape(10.dp),
            elevation = 10.dp,
            backgroundColor = AppTheme.colors.card
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = if (!isShort) 120.dp else 60.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    // 课程名
                    CourseTextField(value = state.courseName,
                        onValueChange = {
                            viewModel.dispatch(CourseEditViewAction.UpdateCourseName(it))
                        },
                        textStyle = TextStyle(fontSize = 14.sp, textAlign = TextAlign.Center),
                        leadingIcon = {
                            Image(
                                painter = painterResource(id = R.drawable.book),
                                contentDescription = "course name",
                                modifier = Modifier.size(25.dp),
                                colorFilter = if (isSystemInDarkTheme()) ColorFilter.tint(Color.Gray)
                                else null
                            )
                        },
                        label = {
                            Text(
                                text = "课程名",
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                color = AppTheme.colors.textDarkColor,
                                modifier = Modifier.width(60.dp)
                            )
                        })
                    // 星期
                    BoxWithConstraints(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(AppTheme.colors.background)
                            .clickable(
                                interactionSource = remember {
                                    MutableInteractionSource()
                                },
                                indication = rememberRipple(),
                                onClick = { expandDInWeek = true })
                            .border(
                                width = 1.dp,
                                color = AppTheme.colors.hintLightColor,
                                shape = RoundedCornerShape(10.dp)
                            )
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(start = 15.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.calendar2),
                                contentDescription = "day in week",
                                modifier = Modifier.size(25.dp),
                                colorFilter = if (isSystemInDarkTheme()) ColorFilter.tint(Color.Gray)
                                else null
                            )
                            Spacer(modifier = Modifier.width(25.dp))
                            Text(
                                text = "星期",
                                fontSize = 16.sp,
                                color = AppTheme.colors.textDarkColor
                            )
                        }
                        Text(
                            text = state.dInWeek,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            color = AppTheme.colors.textDarkColor,
                            modifier = Modifier.padding(start = maxWidth / 4)
                        )
                        DropdownMenu(
                            expanded = expandDInWeek,
                            onDismissRequest = { expandDInWeek = false },
                            offset = DpOffset(maxWidth - 120.dp, 0.dp),
                            modifier = Modifier
                                .background(AppTheme.colors.card)
                                .clip(RoundedCornerShape(10.dp))
                        ) {
                            Column(
                                modifier = Modifier
                                    .height(120.dp)
                                    .verticalScroll(rememberScrollState())
                            ) {
                                for (it in 0..6) {
                                    DropdownMenuItem(
                                        onClick = {
                                            viewModel.dispatch(
                                                CourseEditViewAction.UpdateDInWeek(indexToChar(it))
                                            )
                                            expandDInWeek = false
                                        }, modifier = Modifier.background(AppTheme.colors.card)
                                    ) {
                                        Text(
                                            text = "星期${indexToChar(it)}",
                                            fontSize = 14.sp,
                                            color = AppTheme.colors.textDarkColor
                                        )
                                    }
                                }
                            }
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 20.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.back),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(20.dp)
                                    .rotate(animatedDInWeekRotate),
                                colorFilter = if (isSystemInDarkTheme()) ColorFilter.tint(Color.Gray)
                                else null
                            )
                        }
                    }
                    //位置
                    CourseTextField(value = state.location,
                        onValueChange = { viewModel.dispatch(CourseEditViewAction.UpdateLocation(it)) },
                        textStyle = TextStyle(fontSize = 14.sp, textAlign = TextAlign.Center),
                        leadingIcon = {
                            Image(
                                painter = painterResource(id = R.drawable.location),
                                contentDescription = "location",
                                modifier = Modifier.size(25.dp),
                                colorFilter = if (isSystemInDarkTheme()) ColorFilter.tint(Color.Gray)
                                else null
                            )
                        },
                        label = {
                            Text(
                                text = "位置",
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                color = AppTheme.colors.textDarkColor,
                                modifier = Modifier.width(60.dp)
                            )
                        })
                    // 周数
                    BoxWithConstraints(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(AppTheme.colors.background)
                            .clickable(
                                interactionSource = remember {
                                    MutableInteractionSource()
                                },
                                indication = rememberRipple(),
                                onClick = { expandWeek.value = true })
                            .border(
                                width = 1.dp,
                                color = AppTheme.colors.hintLightColor,
                                shape = RoundedCornerShape(10.dp)
                            )
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(start = 15.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.calendar3),
                                contentDescription = "week",
                                modifier = Modifier.size(25.dp),
                                colorFilter = if (isSystemInDarkTheme()) ColorFilter.tint(Color.Gray)
                                else null
                            )
                            Spacer(modifier = Modifier.width(25.dp))
                            Text(
                                text = "周数",
                                fontSize = 16.sp,
                                color = AppTheme.colors.textDarkColor
                            )
                        }
                        Text(
                            text = "第${state.startWeek}周    至    第${state.endWeek}周",
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            color = AppTheme.colors.textDarkColor,
                            modifier = Modifier.padding(start = maxWidth / 4)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 20.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.back),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(20.dp)
                                    .rotate(animatedWeekRotate),
                                colorFilter = if (isSystemInDarkTheme()) ColorFilter.tint(Color.Gray)
                                else null
                            )
                        }
                    }
                    if (!isShort) {
                        // TODO 节数
                        if (AppContext.courseGlobal.morningClass < -999)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                BoxWithConstraints(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(AppTheme.colors.background)
                                        .clickable(interactionSource = remember {
                                            MutableInteractionSource()
                                        },
                                            indication = rememberRipple(),
                                            onClick = { expandClass = true })
                                        .border(
                                            width = 1.dp,
                                            color = AppTheme.colors.hintLightColor,
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .padding(start = 15.dp)
                                            .fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.calendar3),
                                            contentDescription = "class num",
                                            modifier = Modifier.size(25.dp),
                                            colorFilter = if (isSystemInDarkTheme()) ColorFilter.tint(
                                                Color.Gray
                                            )
                                            else null
                                        )
                                        Spacer(modifier = Modifier.width(25.dp))
                                        Text(
                                            text = "节数",
                                            fontSize = 16.sp,
                                            color = AppTheme.colors.textDarkColor
                                        )
                                    }
                                    Text(
                                        text = "第${classIndex}节",
                                        fontSize = 16.sp,
                                        textAlign = TextAlign.Center,
                                        color = AppTheme.colors.textDarkColor,
                                        modifier = Modifier.padding(start = maxWidth / 4)
                                    )
                                    DropdownMenu(
                                        expanded = expandClass,
                                        onDismissRequest = { expandClass = false },
                                        offset = DpOffset(maxWidth - 120.dp, 0.dp),
                                        modifier = Modifier
                                            .background(AppTheme.colors.card)
                                            .clip(RoundedCornerShape(10.dp))
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .height(120.dp)
                                                .verticalScroll(rememberScrollState())
                                        ) {
                                            for (it in 1..lessonCount) {
                                                DropdownMenuItem(
                                                    onClick = {
                                                        // TODO
//                                                    chooseLesson()
                                                    },
                                                    modifier = Modifier.background(AppTheme.colors.card)
                                                ) {
                                                    Text(
                                                        text = "第${it}节",
                                                        fontSize = 14.sp,
                                                        color = AppTheme.colors.textDarkColor
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(end = 20.dp),
                                        contentAlignment = Alignment.CenterEnd
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.back),
                                            contentDescription = "",
                                            modifier = Modifier
                                                .size(20.dp)
                                                .rotate(animatedClassRotate),
                                            colorFilter = if (isSystemInDarkTheme()) ColorFilter.tint(
                                                Color.Gray
                                            )
                                            else null
                                        )
                                    }
                                }
                            }
                        // 时间
                        BoxWithConstraints(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(AppTheme.colors.background)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = rememberRipple(),
                                    onClick = { expandTime.value = true })
                                .border(
                                    width = 1.dp,
                                    color = AppTheme.colors.hintLightColor,
                                    shape = RoundedCornerShape(10.dp)
                                )
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(start = 15.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.clock),
                                    contentDescription = "time",
                                    modifier = Modifier.size(25.dp),
                                    colorFilter = if (isSystemInDarkTheme()) ColorFilter.tint(
                                        Color.Gray
                                    )
                                    else null
                                )
                                Spacer(modifier = Modifier.width(25.dp))
                                Text(
                                    text = "时间",
                                    fontSize = 16.sp,
                                    color = AppTheme.colors.textDarkColor
                                )
                            }
                            Text(
                                text = "$startH:$startMF$startME    至    $endH:$endMF$endME",
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                color = AppTheme.colors.textDarkColor,
                                modifier = Modifier.padding(start = maxWidth / 4)
                            )
                        }
                        // 闹钟
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            BoxWithConstraints(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .height(56.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(AppTheme.colors.background)
                                    .clickable(interactionSource = remember {
                                        MutableInteractionSource()
                                    },
                                        indication = rememberRipple(),
                                        onClick = { expandAlarm = true })
                                    .border(
                                        width = 1.dp,
                                        color = AppTheme.colors.hintLightColor,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(start = 15.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.alarm),
                                        contentDescription = "alarm",
                                        modifier = Modifier.size(25.dp),
                                        colorFilter = if (isSystemInDarkTheme()) ColorFilter.tint(
                                            Color.Gray
                                        )
                                        else null
                                    )
                                    Spacer(modifier = Modifier.width(25.dp))
                                    Text(
                                        text = "提醒",
                                        fontSize = 16.sp,
                                        color = AppTheme.colors.textDarkColor
                                    )
                                }
                                Text(
                                    text = if (state.alarmTime == 0) "无" else "${state.alarmTime} 分钟前",
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center,
                                    color = AppTheme.colors.textDarkColor,
                                    modifier = Modifier.padding(start = maxWidth / 4)
                                )
                                DropdownMenu(
                                    expanded = expandAlarm,
                                    onDismissRequest = { expandAlarm = false },
                                    offset = DpOffset(maxWidth - 120.dp, 0.dp),
                                    modifier = Modifier
                                        .background(AppTheme.colors.card)
                                        .clip(RoundedCornerShape(10.dp))
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .height(120.dp)
                                            .verticalScroll(rememberScrollState())
                                    ) {
                                        for (it in 0..12) {
                                            DropdownMenuItem(
                                                onClick = {
                                                    viewModel.dispatch(
                                                        CourseEditViewAction.UpdateAlarmTime(it * 5)
                                                    )
                                                    expandAlarm = false
                                                },
                                                modifier = Modifier.background(AppTheme.colors.card)
                                            ) {
                                                Text(
                                                    text = if (it == 0) "无" else "${it * 5} 分钟前",
                                                    fontSize = 14.sp,
                                                    color = AppTheme.colors.textDarkColor
                                                )
                                            }
                                        }
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(end = 20.dp),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.back),
                                        contentDescription = "",
                                        modifier = Modifier
                                            .size(20.dp)
                                            .rotate(animatedAlarmRotate),
                                        colorFilter = if (isSystemInDarkTheme()) ColorFilter.tint(
                                            Color.Gray
                                        )
                                        else null
                                    )
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .padding(end = 10.dp)
                                    .size(40.dp)
                                    .border(
                                        width = 1.dp,
                                        color = AppTheme.colors.hintDarkColor,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable(
                                        interactionSource = MutableInteractionSource(),
                                        indication = null
                                    ) {
                                        if (state.isAlarm) viewModel.dispatch(
                                            CourseEditViewAction.UpdateAlarmTime(0)
                                        )
                                        else viewModel.dispatch(
                                            CourseEditViewAction.UpdateAlarmTime(5)
                                        )
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                if (state.isAlarm) {
                                    val isNight = isSystemInDarkTheme()
                                    Canvas(modifier = Modifier.size(15.dp), onDraw = {
                                        drawCircle(
                                            color = if (isNight) SchoolBlueNight else SchoolBlueDay,
                                            style = Stroke(width = 5f)
                                        )
                                    })
                                }
                            }
                        }
                    }
                }
            }
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Divider(
                        Modifier.fillMaxWidth(),
                        color = AppTheme.colors.hintLightColor,
                        thickness = 1.dp
                    )
                    Box(
                        modifier = Modifier
                            .height(60.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
                            .clickable(indication = rememberRipple(),
                                interactionSource = MutableInteractionSource(),
                                onClick = {
                                    viewModel.dispatch(CourseEditViewAction.SaveCourse())
                                }),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "保存",
                            fontSize = 18.sp,
                            color = AppTheme.colors.textBlackColor
                        )
                    }
                }
            }
        }
    }


    BottomBaseDialog(content = {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .padding(vertical = 40.dp)
        ) {
            Text(text = "第", fontSize = 14.sp, color = AppTheme.colors.textDarkColor)
            FVerticalWheelPicker(
                count = 24,
                modifier = Modifier.width(30.dp),
                state = weekSState
            ) {
                Text(text = "${it + 1}", fontSize = 14.sp, color = AppTheme.colors.textDarkColor)
            }
            Text(text = "周  至  第", fontSize = 14.sp, color = AppTheme.colors.textDarkColor)
            FVerticalWheelPicker(
                count = 24 - state.startWeek,
                modifier = Modifier.width(30.dp),
                state = weekEState
            ) {
                Text(
                    text = "${it + state.startWeek}",
                    fontSize = 14.sp,
                    color = AppTheme.colors.textDarkColor
                )
            }
            Text(text = "周", fontSize = 14.sp, color = AppTheme.colors.textDarkColor)
        }
    }, items = listOf(), isShow = expandWeek)

    BottomBaseDialog(content = {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(vertical = 40.dp)
        ) {
            FVerticalWheelPicker(
                count = 24,
                modifier = Modifier.width(30.dp),
                state = startTimeHState
            ) {
                Text(text = "$it", fontSize = 14.sp, color = AppTheme.colors.textDarkColor)
            }
            Text(text = ":", fontSize = 14.sp, color = AppTheme.colors.textDarkColor)
            FVerticalWheelPicker(
                count = 6,
                modifier = Modifier.width(30.dp),
                state = startTimeMFState
            ) {
                Text(text = "$it", fontSize = 14.sp, color = AppTheme.colors.textDarkColor)
            }
            FVerticalWheelPicker(
                count = 10,
                modifier = Modifier.width(30.dp),
                state = startTimeMEState
            ) {
                Text(text = "$it", fontSize = 14.sp, color = AppTheme.colors.textDarkColor)
            }
            Text(text = "  到  ", fontSize = 14.sp, color = AppTheme.colors.textDarkColor)
            FVerticalWheelPicker(
                count = 24,
                modifier = Modifier.width(30.dp),
                state = endTimeHState
            ) {
                Text(
                    text = "$it",
                    fontSize = 14.sp,
                    color = AppTheme.colors.textDarkColor
                )
            }
            Text(text = ":", fontSize = 14.sp, color = AppTheme.colors.textDarkColor)
            FVerticalWheelPicker(
                count = 6,
                modifier = Modifier.width(30.dp),
                state = endTimeMFState
            ) {
                Text(
                    text = "$it",
                    fontSize = 14.sp,
                    color = AppTheme.colors.textDarkColor
                )
            }
            FVerticalWheelPicker(
                count = 10,
                modifier = Modifier.width(30.dp),
                state = endTimeMEState
            ) {
                Text(
                    text = "$it",
                    fontSize = 14.sp,
                    color = AppTheme.colors.textDarkColor
                )
            }
        }
    }, items = listOf(), isShow = expandTime)
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFAFAFA)
private fun CourseEditPagePreview() {
    CourseEditPage(null, null, onBack = {}, navToCourseGlobal = {})
}