package com.lemon.smartcampus.ui.course

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.lemon.smartcampus.R
import com.lemon.smartcampus.ui.theme.AppTheme
import com.lemon.smartcampus.ui.widges.ColoredTitleBar
import com.lemon.smartcampus.ui.widges.GlobalSettingOutlinedTextField
import com.lemon.smartcampus.ui.widges.SNACK_ERROR
import com.lemon.smartcampus.ui.widges.popupSnackBar
import com.lemon.smartcampus.viewModel.course.CourseGlobalViewAction
import com.lemon.smartcampus.viewModel.course.CourseGlobalViewEvent
import com.lemon.smartcampus.viewModel.course.CourseGlobalViewModel
import com.zj.mvi.core.observeEvent

@Composable
fun CourseGlobalPage(
    navController: NavController?,
    scaffoldState: ScaffoldState?,
    viewModel: CourseGlobalViewModel = viewModel()
) {
    var shortHeight by remember { mutableStateOf(false) }
    var saveStep by remember { mutableStateOf(false) }

    var nowClassExpand by remember { mutableStateOf(false) }
    var classCount by remember { mutableStateOf(0) }

    val state by viewModel.viewStates.collectAsState()
    val scope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current

    val animatedAlarmRotate by animateFloatAsState(targetValue = if (nowClassExpand) 90f else 270f)
    LaunchedEffect(key1 = Unit) {
        viewModel.dispatch(viewAction = CourseGlobalViewAction.LoadSetting)
        viewModel.viewEvents.observeEvent(lifecycleOwner) { event ->
            when (event) {
                is CourseGlobalViewEvent.ShowToast -> scaffoldState?.let {
                    popupSnackBar(
                        scaffoldState = scaffoldState,
                        scope = scope,
                        label = SNACK_ERROR,
                        message = event.msg
                    )
                }
                is CourseGlobalViewEvent.TransIntent -> navController?.popBackStack()
                is CourseGlobalViewEvent.CanNext -> nowClassExpand = true
            }
        }
        scaffoldState?.let { popupSnackBar(scope, scaffoldState, SNACK_ERROR, "此功能尚未实现!") }
    }

    LaunchedEffect(key1 = state.morningClass, key2 = state.noonClass, key3 = state.nightClass) {
        classCount = (state.morningClass ?: 0) + (state.noonClass ?: 0) + (state.nightClass ?: 0)
    }

    BoxWithConstraints(Modifier.fillMaxSize()) {
        shortHeight = maxHeight <= 600.dp
    }
    Column(
        Modifier
            .fillMaxSize()
            .padding(bottom = if (!shortHeight) 30.dp else 0.dp)
    ) {
        if (!shortHeight) ColoredTitleBar(
            color = if (!isSystemInDarkTheme()) Color(0xFFFFF3D8) else Color(0xFF635D53),
            text = "全局设置"
        ) { navController?.popBackStack() }
        if (!saveStep)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(if (!shortHeight) 0.9f else 1f)
                    .padding(horizontal = 20.dp, vertical = if (!shortHeight) 20.dp else 0.dp),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GlobalSettingOutlinedTextField(
                    value = state.totalWeek?.toString() ?: "", onValueChange = {
                        if (it.length <= 3) viewModel.dispatch(
                            CourseGlobalViewAction.UpdateTotalWeek(
                                it.toIntOrNull()
                            )
                        )
                    }, leadingIcon = R.drawable.calendar2, placeholder = "学期周数"
                )
                GlobalSettingOutlinedTextField(
                    value = state.singleTime?.toString() ?: "",
                    onValueChange = {
                        if (it.length <= 3) viewModel.dispatch(
                            CourseGlobalViewAction.UpdateSingleTime(
                                it.toIntOrNull()
                            )
                        )
                    },
                    leadingIcon = R.drawable.clock,
                    trailingIcon = {
                        Text(
                            text = "分钟",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            color = if (state.singleTime == null) AppTheme.colors.textLightColor
                            else AppTheme.colors.textBlackColor,
                            modifier = Modifier.padding(end = 10.dp)
                        )
                    },
                    placeholder = "单节课时长"
                )
                GlobalSettingOutlinedTextField(
                    value = state.breakTime?.toString() ?: "",
                    onValueChange = {
                        if (it.length <= 2) viewModel.dispatch(
                            CourseGlobalViewAction.UpdateBreakTime(
                                it.toIntOrNull()
                            )
                        )
                    },
                    leadingIcon = R.drawable.clock,
                    trailingIcon = {
                        Text(
                            text = "分钟",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            color = if (state.breakTime == null) AppTheme.colors.textLightColor
                            else AppTheme.colors.textBlackColor,
                            modifier = Modifier.padding(end = 10.dp)
                        )
                    },
                    placeholder = "课间时长"
                )
                GlobalSettingOutlinedTextField(
                    value = state.morningClass?.toString() ?: "",
                    onValueChange = {
                        if (it.length <= 2) viewModel.dispatch(
                            CourseGlobalViewAction.UpdateMorningClass(
                                it.toIntOrNull()
                            )
                        )
                    },
                    leadingIcon = R.drawable.calendar,
                    trailingIcon = {
                        Text(
                            text = "节课",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            color = if (state.morningClass == null) AppTheme.colors.textLightColor
                            else AppTheme.colors.textBlackColor,
                            modifier = Modifier.padding(end = 10.dp)
                        )
                    },
                    placeholder = "上午课程节数"
                )
                GlobalSettingOutlinedTextField(
                    value = state.noonClass?.toString() ?: "",
                    onValueChange = {
                        if (it.length <= 2) viewModel.dispatch(
                            CourseGlobalViewAction.UpdateNoonClass(
                                it.toIntOrNull()
                            )
                        )
                    },
                    leadingIcon = R.drawable.calendar,
                    trailingIcon = {
                        Text(
                            text = "节课",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            color = if (state.noonClass == null) AppTheme.colors.textLightColor
                            else AppTheme.colors.textBlackColor,
                            modifier = Modifier.padding(end = 10.dp)
                        )
                    },
                    placeholder = "下午课程节数"
                )
                if ((state.noonClass ?: 0) > 0) {
                    GlobalSettingOutlinedTextField(
                        value = state.nightClass?.toString() ?: "",
                        onValueChange = {
                            if (it.length <= 2) viewModel.dispatch(
                                CourseGlobalViewAction.UpdateNightClass(
                                    it.toIntOrNull()
                                )
                            )
                        },
                        leadingIcon = R.drawable.calendar,
                        trailingIcon = {
                            Text(
                                text = "节课",
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                color = if (state.nightClass == null) AppTheme.colors.textLightColor
                                else AppTheme.colors.textBlackColor,
                                modifier = Modifier.padding(end = 10.dp)
                            )
                        },
                        placeholder = "晚上课程节数",
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        )
                    )
                    GlobalSettingOutlinedTextField(
                        value = state.noonBreakTime?.toString() ?: "",
                        onValueChange = {
                            if (it.length <= 3) viewModel.dispatch(
                                CourseGlobalViewAction.UpdateNoonBreakTime(
                                    it.toIntOrNull()
                                )
                            )
                        },
                        leadingIcon = R.drawable.clock,
                        trailingIcon = {
                            Text(
                                text = "分钟",
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                color = if (state.noonBreakTime == null) AppTheme.colors.textLightColor
                                else AppTheme.colors.textBlackColor,
                                modifier = Modifier.padding(end = 10.dp)
                            )
                        },
                        placeholder = "午休时间",
                        keyboardOptions = KeyboardOptions(
                            imeAction = if ((state.nightClass
                                    ?: 0) > 0
                            ) ImeAction.Next else ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = {
                            viewModel.dispatch(CourseGlobalViewAction.SaveSetting)
                        })
                    )
                    if ((state.nightClass ?: 0) > 0)
                        GlobalSettingOutlinedTextField(
                            value = state.nightBreakTime?.toString() ?: "",
                            onValueChange = {
                                if (it.length <= 3) viewModel.dispatch(
                                    CourseGlobalViewAction.UpdateNightBreakTime(
                                        it.toIntOrNull()
                                    )
                                )
                            },
                            leadingIcon = R.drawable.clock,
                            trailingIcon = {
                                Text(
                                    text = "分钟",
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center,
                                    color = if (state.nightBreakTime == null) AppTheme.colors.textLightColor
                                    else AppTheme.colors.textBlackColor,
                                    modifier = Modifier.padding(end = 10.dp)
                                )
                            },
                            placeholder = "晚休时间",
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = {
                                viewModel.dispatch(CourseGlobalViewAction.SaveSetting)
                            })
                        )
                }
            }
        else
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(if (!shortHeight) 0.9f else 1f)
                    .padding(horizontal = 20.dp, vertical = if (!shortHeight) 20.dp else 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 选择时间
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
                                onClick = { viewModel.dispatch(CourseGlobalViewAction.UpdateClassTimeMap) })
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
                            text = "第 ${state.editClassIndex} 节",
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            color = AppTheme.colors.textDarkColor,
                            modifier = Modifier.padding(start = maxWidth / 4)
                        )
                        DropdownMenu(
                            expanded = nowClassExpand,
                            onDismissRequest = { nowClassExpand = false },
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
                                for (it in 1..classCount) {
                                    DropdownMenuItem(
                                        onClick = {
                                            viewModel.dispatch(
                                                CourseGlobalViewAction.UpdateEditClassIndex(
                                                    it
                                                )
                                            )
                                            nowClassExpand = false
                                        },
                                        modifier = Modifier.background(AppTheme.colors.card)
                                    ) {
                                        Text(
                                            text = "第 $it 节",
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
                                colorFilter = if (isSystemInDarkTheme()) ColorFilter.tint(Color.Gray)
                                else null
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    GlobalSettingOutlinedTextField(
                        value = state.startTime,
                        onValueChange = {
                            viewModel.dispatch(CourseGlobalViewAction.UpdateStartTime(it))
                        },
                        placeholder = "开始时间",
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    GlobalSettingOutlinedTextField(
                        value = state.endTime,
                        onValueChange = {
                            viewModel.dispatch(CourseGlobalViewAction.UpdateEndTime(it))
                        },
                        placeholder = "结束时间",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        if (!shortHeight)
            Box(
                modifier = Modifier
                    .padding(top = 10.dp, start = 20.dp, end = 20.dp)
                    .fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    if (saveStep) {
                        Card(
                            modifier = Modifier
                                .weight(1f),
                            elevation = 10.dp,
                            shape = RoundedCornerShape(10.dp),
                            backgroundColor = AppTheme.colors.card
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable(interactionSource = remember { MutableInteractionSource() },
                                        indication = rememberRipple(),
                                        onClick = { saveStep = false }),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "返回",
                                    fontSize = 18.sp,
                                    color = AppTheme.colors.textBlackColor,
                                    modifier = Modifier.padding(vertical = 15.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(20.dp))
                    }
                    Card(
                        modifier = Modifier
                            .weight(1f),
                        elevation = 10.dp,
                        shape = RoundedCornerShape(10.dp),
                        backgroundColor = AppTheme.colors.card
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .clickable(interactionSource = remember { MutableInteractionSource() },
                                    indication = rememberRipple(),
                                    onClick = {
                                        if (saveStep)
                                            viewModel.dispatch(CourseGlobalViewAction.SaveSetting)
                                        else saveStep = true
                                    }),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (saveStep) "保存" else "下一步",
                                fontSize = 18.sp,
                                color = AppTheme.colors.textBlackColor,
                                modifier = Modifier.padding(vertical = 15.dp)
                            )
                        }
                    }
                }
            }
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFAFAFA)
private fun CourseGlobalPagePreview() {
    CourseGlobalPage(navController = null, scaffoldState = null)
}
