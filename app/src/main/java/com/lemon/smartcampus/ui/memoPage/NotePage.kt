package com.lemon.smartcampus.ui.memoPage

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.lemon.smartcampus.R
import com.lemon.smartcampus.data.database.entities.NoteEntity
import com.lemon.smartcampus.ui.theme.AppTheme
import com.lemon.smartcampus.ui.widges.*
import com.lemon.smartcampus.viewModel.note.NoteViewAction
import com.lemon.smartcampus.viewModel.note.NoteViewEvent
import com.lemon.smartcampus.viewModel.note.NoteViewModel
import com.zj.mvi.core.observeEvent
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalLayoutApi::class, ExperimentalAnimationApi::class)
@Composable
fun NotePage(
    navController: NavController?,
    scaffoldState: ScaffoldState?,
    viewModel: NoteViewModel = viewModel()
) {
    val state by viewModel.viewStates.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()
    var time by remember { mutableStateOf("") }
    val contentLength by rememberUpdatedState(newValue = state.content.length)

    val firstRow = remember { mutableStateListOf<NoteEntity>() }
    val secondRow = remember { mutableStateListOf<NoteEntity>() }
    val scrollState = rememberScrollState()

    var changeMode by remember { mutableStateOf(false) }
    var isShowMore by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = Unit) {
        viewModel.dispatch(NoteViewAction.GetNoteList)
        viewModel.viewEvents.observeEvent(lifecycleOwner) { events ->
            when (events) {
                is NoteViewEvent.ShowToast -> scaffoldState?.let {
                    popupSnackBar(
                        scope,
                        scaffoldState,
                        if (events.success) SNACK_SUCCESS else SNACK_ERROR,
                        events.msg
                    )
                }
            }
        }
    }
    LaunchedEffect(key1 = state.selectedEntity) {
        time = state.selectedEntity.createTime.ifBlank {
            SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", Locale.PRC).format(System.currentTimeMillis())
        }
    }
    LaunchedEffect(key1 = state.noteList) {
        firstRow.clear()
        firstRow.addAll(state.noteList.filterIndexed { index, _ -> index % 2 == 0 })
        secondRow.clear()
        secondRow.addAll(state.noteList.filterIndexed { index, _ -> index % 2 == 1 })
    }
    Scaffold(topBar = {
        ColoredTitleBar(
            color = if (!isSystemInDarkTheme()) Color(0xFFD0F2FF)
            else Color(0xFF6D7F86), text = "我的备忘"
        ) { navController?.popBackStack() }
    },
        floatingActionButton = {
            FloatingActionButton(modifier = Modifier.padding(bottom = 40.dp, end = 20.dp),
                backgroundColor = AppTheme.colors.card,
                onClick = { changeMode = true }) {
                Image(
                    painter = painterResource(id = R.drawable.plus),
                    contentDescription = "create note",
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                )
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .clickable(enabled = isShowMore, interactionSource = remember {
                MutableInteractionSource()
            }, indication = null, onClick = { isShowMore = false }),
        backgroundColor = AppTheme.colors.background
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(start = 10.dp, end = 10.dp, top = 20.dp)
                .verticalScroll(scrollState),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                for (item in firstRow) {
                    NoteCard(noteEntity = item, modifier = Modifier.fillMaxWidth(0.9f)) {
                        viewModel.dispatch(NoteViewAction.UpdateEntity(item))
                        changeMode = true
                    }
                    if (item != firstRow.last()) Spacer(modifier = Modifier.height(20.dp))
                }
            }
            Column(
                modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                for (item in secondRow) {
                    NoteCard(noteEntity = item, modifier = Modifier.fillMaxWidth(0.9f)) {
                        viewModel.dispatch(NoteViewAction.UpdateEntity(item))
                        changeMode = true
                    }
                    if (item != secondRow.last()) Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
    AnimatedVisibility(
        visible = changeMode,
        enter = fadeIn(tween(400, 0)) + slideIn(
            initialOffset = { fullSize ->
                IntOffset(fullSize.width / 3, 4 * fullSize.height / 5)
            }, animationSpec = tween(600, 100)
        ) + expandIn(animationSpec = tween(600, 0)) { IntSize.Zero },
        exit = fadeOut(tween(600, 100)) + slideOut(
            targetOffset = { fullSize ->
                IntOffset(-fullSize.width, -fullSize.height)
            }, animationSpec = tween(600, 0)
        )
    ) {
        Scaffold(modifier = Modifier
            .fillMaxSize()
            .clickable(enabled = isShowMore, interactionSource = remember {
                MutableInteractionSource()
            }, indication = null, onClick = { isShowMore = false }),
            backgroundColor = AppTheme.colors.background,
            topBar = {
                ToolBarMore(onMore = {
                    isShowMore = true
                }, onBack = {
                    changeMode = false
                    viewModel.dispatch(NoteViewAction.PostNote)
                })
            }) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(top = 20.dp)
            ) {
                TextField(
                    value = state.title,
                    placeholder = {
                        Text(
                            text = "标题", fontSize = 20.sp, color = AppTheme.colors.textDarkColor
                        )
                    },
                    onValueChange = { viewModel.dispatch(NoteViewAction.UpdateTitle(it)) },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = AppTheme.colors.textBlackColor,
                        backgroundColor = Color.Transparent,
                        cursorColor = AppTheme.colors.schoolBlue,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontSize = 20.sp),
                    maxLines = 1
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                    thickness = 1.dp,
                    color = AppTheme.colors.hintDarkColor
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(modifier = Modifier.padding(horizontal = 15.dp)) {
                    Text(text = time, fontSize = 12.sp, color = AppTheme.colors.textLightColor)
                    Spacer(modifier = Modifier.width(30.dp))
                    Text(
                        text = "${contentLength}字",
                        fontSize = 12.sp,
                        color = AppTheme.colors.textLightColor
                    )
                }
                TextField(
                    value = state.content,
                    placeholder = {
                        Text(
                            text = "开始书写", fontSize = 16.sp, color = AppTheme.colors.textDarkColor
                        )
                    },
                    onValueChange = { viewModel.dispatch(NoteViewAction.UpdateContent(it)) },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = AppTheme.colors.textBlackColor,
                        backgroundColor = Color.Transparent,
                        cursorColor = AppTheme.colors.schoolBlue,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxSize(),
                    textStyle = TextStyle(fontSize = 16.sp)
                )
            }
        }
    }
    BackHandler(enabled = changeMode) {
        changeMode = false
        viewModel.dispatch(NoteViewAction.PostNote)
    }
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
        MoreActionCard(listOf(ActionPair("设置提醒") {
            isShowMore = false
        }, ActionPair("删除") {
            viewModel.dispatch(NoteViewAction.DelNote)
            changeMode = false
            isShowMore = false
        }), isShowMore)
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFAFAFA)
private fun NotePagePreview() {
    NotePage(navController = null, scaffoldState = null)
}