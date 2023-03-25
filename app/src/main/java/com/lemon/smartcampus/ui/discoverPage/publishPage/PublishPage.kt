package com.lemon.smartcampus.ui.discoverPage.publishPage

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.lemon.smartcampus.R
import com.lemon.smartcampus.ui.theme.AppTheme
import com.lemon.smartcampus.ui.widges.*
import com.lemon.smartcampus.utils.AUTH_PAGE
import com.lemon.smartcampus.utils.uri2Path
import com.lemon.smartcampus.viewModel.topic.publish.PublishViewAction
import com.lemon.smartcampus.viewModel.topic.publish.PublishViewEvent
import com.lemon.smartcampus.viewModel.topic.publish.PublishViewModel
import com.orhanobut.logger.Logger
import com.zj.mvi.core.observeEvent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File

val tabList = listOf("话题", "资源")

private object PageList {
    private var pageList: List<@Composable () -> Unit> = listOf()
    fun getPage(
        index: Int,
        navController: NavController?,
        scaffoldState: ScaffoldState?,
        viewModel: PublishViewModel
    ): @Composable () -> Unit {
        if (pageList.isEmpty()) pageList = listOf(
            { TopicPublishPage(scaffoldState = scaffoldState, viewModel = viewModel) },
            { ResPublishPage(scaffoldState = scaffoldState, viewModel = viewModel) },
        )
        return pageList[index]
    }
}

@OptIn(ExperimentalPagerApi::class, ExperimentalComposeUiApi::class)
@Composable
fun PublishPage(
    navController: NavController?,
    scaffoldState: ScaffoldState?,
    viewModel: PublishViewModel = viewModel()
) {
    var topicMode by remember { mutableStateOf(true) }
    var nowSelect by remember { mutableStateOf(0) }
    var loading by remember { mutableStateOf(false) }
    var filePath by remember { mutableStateOf("") }

    val isShowPermission = remember { mutableStateOf(false) }
    val animateWidth by animateDpAsState(
        targetValue = if (filePath.isNotBlank()) 220.dp else 50.dp,
        tween(300)
    )
    val animateRotate by animateFloatAsState(
        targetValue = if (filePath.isNotBlank()) 405f else 0f,
        tween(300)
    )

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current
    val state by viewModel.viewStates.collectAsState()
    LaunchedEffect(key1 = Unit) {
        viewModel.viewEvents.observeEvent(lifecycleOwner) { event ->
            when (event) {
                is PublishViewEvent.ShowToast -> {
                    scaffoldState?.let {
                        popupSnackBar(
                            scope = scope,
                            scaffoldState = scaffoldState,
                            if (event.success) SNACK_SUCCESS else SNACK_ERROR,
                            event.msg
                        )
                    }
                }
                is PublishViewEvent.TransIntent -> navController?.popBackStack()
                is PublishViewEvent.ShowLoadingDialog -> loading = true
                is PublishViewEvent.DismissLoadingDialog -> loading = false
                is PublishViewEvent.Logout -> navController?.navigate(AUTH_PAGE) {
                    popUpToRoute
                    launchSingleTop
                }
            }
        }
    }
    fun popMsg(hint: String, msg: String) {
        scaffoldState?.let {
            popupSnackBar(scope, it, hint, message = msg)
        }
    }

    DisposableEffect(key1 = Unit) {
        onDispose {
            viewModel.dispatch(PublishViewAction.UpdateContent(""))
            viewModel.dispatch(PublishViewAction.UpdateTags(listOf()))
        }
    }

    val imeController = LocalSoftwareKeyboardController.current
    val pageState = rememberPagerState()

    LaunchedEffect(key1 = pageState) {
        snapshotFlow { pageState.currentPage }.onEach { nowSelect = it }.collect()
    }
    LaunchedEffect(key1 = nowSelect) {
        scope.launch { pageState.animateScrollToPage(nowSelect) }
        imeController?.hide()
        topicMode = nowSelect == 0
    }
    LaunchedEffect(key1 = WindowInsets.ime) {
        Logger.d("change")
    }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            uri?.let {
                val path = uri2Path(context = context, uri)
                path?.let {
                    viewModel.dispatch(PublishViewAction.UpdatePath(path))
                    filePath = path
                } ?: popMsg(SNACK_ERROR, "文件获取失败")
            }
        }

    Column(Modifier.fillMaxSize()) {
        ToolBarMore(rightIcon = {
            Image(
                painter = painterResource(id = R.drawable.send),
                contentDescription = "send",
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable(indication = rememberRipple(),
                        interactionSource = MutableInteractionSource(),
                        onClick = {
                            val tags = Regex("#.*?#")
                                .findAll(state.content)
                                .map { it.value.replace("#", "") }
                                .filter { it.isNotBlank() && it.length <= 4 }
                                .toSet()
                                .toList()
                            Logger.d("content: ${state.content}")
                            viewModel.dispatch(
                                PublishViewAction.UpdateTags(
                                    if (tags.size > 3) tags.subList(0, 3) else tags
                                )
                            )
                            viewModel.dispatch(PublishViewAction.Publish(topicMode))
                        })
                    .size(30.dp)
                    .padding(5.dp)
            )
        }, onBack = { navController?.popBackStack() })
        TabTitle(
            tabList = tabList, onClick = { nowSelect = it }, nowSelect = nowSelect
        )
        Scaffold(modifier = Modifier.fillMaxSize(), floatingActionButton = {
            if (nowSelect == 1) {
                // 添加资源
                Card(
                    modifier = Modifier
                        .height(50.dp)
                        .width(animateWidth)
                        .clip(RoundedCornerShape(100.dp)),
                    elevation = 10.dp,
                    backgroundColor = AppTheme.colors.card
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FloatingActionButton(
                            onClick = {
                                if (filePath.isBlank()) isShowPermission.value = true
                                else filePath = ""
                            },
                            shape = CircleShape,
                            backgroundColor = AppTheme.colors.card,
                            modifier = Modifier
                                .size(50.dp)
                                .rotate(animateRotate)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.plus),
                                contentDescription = "add resource",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(10.dp)
                                    .clip(CircleShape)
                            )
                        }
                        if (state.path.isNotBlank()) Spacer(modifier = Modifier.width(10.dp))
                        Row(
                            modifier = Modifier
                                .width(animateWidth)
                                .padding(end = 20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (state.path.isNotBlank()) {
                                Text(
                                    text = File(state.path).name,
                                    modifier = Modifier.width(80.dp),
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 14.sp,
                                    maxLines = 1,
                                    color = AppTheme.colors.textBlackColor
                                )
                                Text(
                                    text = "类型: ",
                                    fontSize = 14.sp,
                                    maxLines = 1,
                                    color = AppTheme.colors.textBlackColor
                                )
                                Text(
                                    text = File(state.path).extension,
                                    fontSize = 14.sp,
                                    color = AppTheme.colors.textBlackColor,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }) { padding ->
            HorizontalPager(
                count = tabList.size,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                state = pageState
            ) {
                PageList.getPage(it, navController, scaffoldState, viewModel).invoke()
            }
        }
    }
    if (loading) WarpLoadingDialog()

    GrantPermission(isShow = isShowPermission,
        permission = PermissionType.READ,
        textDenied = "应用程序需要申请  \"文件读取\"  权限\n来获取您要上传的文件",
        textBlock = "应用程序需要申请  \"文件读取\"  权限",
        doAfterPermission = {
            launcher.launch(arrayOf("*/*"))
            isShowPermission.value = false
        })
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFAFAFA)
private fun PublishPagePreview() {
    PublishPage(navController = null, scaffoldState = null)
}