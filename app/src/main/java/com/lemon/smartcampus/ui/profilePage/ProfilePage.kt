package com.lemon.smartcampus.ui.profilePage

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.lemon.smartcampus.BuildConfig
import com.lemon.smartcampus.R
import com.lemon.smartcampus.data.database.entities.ProfileEntity
import com.lemon.smartcampus.data.globalData.AppContext
import com.lemon.smartcampus.ui.theme.AppTheme
import com.lemon.smartcampus.ui.widges.*
import com.lemon.smartcampus.utils.*
import com.lemon.smartcampus.viewModel.profile.ProfileViewAction
import com.lemon.smartcampus.viewModel.profile.ProfileViewEvent
import com.lemon.smartcampus.viewModel.profile.ProfileViewModel
import com.orhanobut.logger.Logger
import com.zj.mvi.core.observeEvent
import github.leavesczy.matisse.Matisse
import github.leavesczy.matisse.MatisseContract
import java.io.File

@Composable
fun ProfilePage(
    navController: NavController?,
    scaffoldState: ScaffoldState?,
    viewModel: ProfileViewModel = viewModel()
) {
    val profile: ProfileEntity by rememberUpdatedState(newValue = AppContext.profile!!)
    val context = LocalContext.current

    val state by viewModel.viewStates.collectAsState()
    val scope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current

    var isLoading by remember { mutableStateOf(false) }

    var recompose by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = Unit) {
        viewModel.dispatch(ProfileViewAction.GetUserPost)
        viewModel.viewEvents.observeEvent(lifecycleOwner) {
            when (it) {
                is ProfileViewEvent.ShowToast -> scaffoldState?.let { scaffold ->
                    popupSnackBar(
                        scope, scaffold, SNACK_ERROR, it.msg
                    )
                }
                is ProfileViewEvent.Recompose -> recompose = true
                is ProfileViewEvent.ShowLoadingDialog -> isLoading = true
                is ProfileViewEvent.DismissLoadingDialog -> isLoading = false
                is ProfileViewEvent.Logout -> navController?.navigate(AUTH_PAGE) {
                    popUpToRoute
                    launchSingleTop
                }
            }

        }
    }

    var avatarMode by remember { mutableStateOf(true) }
    var albumMode by remember { mutableStateOf(true) }
    val oldLauncher = rememberLauncherForActivityResult(MatisseContract()) {
        if (it.isNotEmpty()) {
            val mediaResource = it[0]
            val imageUri = mediaResource.uri
            val imagePath = mediaResource.path
            if (avatarMode)
                viewModel.dispatch(ProfileViewAction.ChangeAvatar(imagePath))
            else
                viewModel.dispatch(ProfileViewAction.ChangeBackground(imagePath))
        }
    }
    val newLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
            uri?.let {
                context.contentResolver.takePersistableUriPermission(uri, flag)
                uri2Path(context, uri)?.let { path ->
                    if (avatarMode)
                        viewModel.dispatch(ProfileViewAction.ChangeAvatar(path))
                    else
                        viewModel.dispatch(ProfileViewAction.ChangeBackground(path))
                } ?: scaffoldState?.let { popupSnackBar(scope, it, SNACK_ERROR, "获取文件失败") }
            } ?: scaffoldState?.let { popupSnackBar(scope, it, SNACK_ERROR, "获取文件失败") }
        }

    var photoUri by remember { mutableStateOf(Uri.EMPTY) }
    val photoLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            Logger.d("$photoUri, take: $it")
            if (it) {
                val fileName =
                    photoUri.path?.substring(photoUri.path?.lastIndexOf("/")?.plus(1) ?: 0)
                var photoPath = "${context.cacheDir}/$fileName"
                val presetFile = File(photoPath)
                // 大于3M的图片处理
                if (presetFile.length() > 3 * 1024 * 1024)
                    photoPath = saveBitmapToFile(presetFile)?.absolutePath ?: ""

                if (avatarMode)
                    viewModel.dispatch(ProfileViewAction.ChangeAvatar(photoPath))
                else
                    viewModel.dispatch(ProfileViewAction.ChangeBackground(photoPath))
            }
        }


    // 刷新界面
    LaunchedEffect(key1 = recompose) { if (recompose) recompose = false }

    val animateAlpha by animateFloatAsState(targetValue = if (recompose) 0.99f else 1f)
    var showSetting by remember { mutableStateOf(false) }

    val showBottomDialog = remember { mutableStateOf(false) }
    val showGetPermission = remember { mutableStateOf(false) }
    val showEditPage = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(indication = null,
                interactionSource = MutableInteractionSource(),
                onClick = { showSetting = false })
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .alpha(animateAlpha)
        ) {
            // 背景图
            Box(modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = rememberRipple()
                ) {
                    if (profile.id.isBlank()) navController?.navigate(AUTH_PAGE) {
                        launchSingleTop = true
                    }
                    else {
                        avatarMode = false
                        showBottomDialog.value = true
                    }
                }) {
                if (profile.background.isBlank()) Image(
                    painter = painterResource(id = R.drawable.unlogin_profile_bg),
                    contentDescription = "bg",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    colorFilter = if (isSystemInDarkTheme()) ColorFilter.tint(
                        Color.Gray,
                        BlendMode.Multiply
                    ) else null
                )
                else AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(profile.background)
                        .build(),
                    contentDescription = "bg",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    colorFilter = if (isSystemInDarkTheme()) ColorFilter.tint(
                        Color.Gray,
                        BlendMode.Multiply
                    ) else null
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = maxWidth * (9 / 32f) + 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfileCard(iconUrl = profile.avatar,
                    nickname = profile.nickname,
                    tags = profile.tags,
                    onEdit = {
                        if (profile.id.isBlank()) navController?.navigate(AUTH_PAGE) {
                            launchSingleTop = true
                        }
                        else showEditPage.value = true
                    },
                    onChangeArthur = {
                        if (profile.id.isBlank()) navController?.navigate(AUTH_PAGE) {
                            launchSingleTop = true
                        } else {
                            avatarMode = true
                            showBottomDialog.value = true
                        }
                    })
                Spacer(modifier = Modifier.height(20.dp))
                // 按钮组
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ProfileBtn(
                        img = R.drawable.calendar2,
                        text = "我的课程",
                        backgroundColor = if (!isSystemInDarkTheme()) Color(0xFFFFF3D8)
                        else Color(0xFF635D53)
                    ) {
                        navController?.navigate(COURSE_PAGE) { launchSingleTop }
                    }
                    ProfileBtn(
                        img = R.drawable.memo,
                        text = "我的备忘",
                        backgroundColor = if (!isSystemInDarkTheme()) Color(0xFFD0F2FF)
                        else Color(0xFF6D7F86)
                    ) {
                        // TODO 我的备忘
                        // TODO 开发警告
                        scaffoldState?.let {
                            popupSnackBar(
                                scope,
                                scaffoldState,
                                SNACK_WARN,
                                "!!!注意!!!该功能正在开发或者测试当中"
                            )
                        }
                    }
                }
                // 我的发布
                if (state.resList.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 23.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(AppTheme.colors.card)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp, top = 5.dp)
                        ) {
                            Text(
                                text = "我的发布",
                                fontSize = 14.sp,
                                color = AppTheme.colors.textDarkColor
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp, end = 10.dp, bottom = 20.dp)
                        ) {
                            items(state.resList) { res ->
                                ProfileResCard(resName = res.resourceName,
                                    resType = res.resourceType,
                                    resSize = res.resourceSize,
                                    resLink = res.resourceLink,
                                    onClick = {
                                        AppContext.topicDetail += mapOf(Pair(res.topicId, res))
                                        navController?.navigate("$DETAILS_PAGE/${res.topicId}")
                                    },
                                    onDownload = {
                                        // TODO 下载资源
                                        // TODO 开发警告
                                        scaffoldState?.let {
                                            popupSnackBar(
                                                scope,
                                                scaffoldState,
                                                SNACK_WARN,
                                                "!!!注意!!!该功能正在开发或者测试当中"
                                            )
                                        }
                                    })
                                Spacer(modifier = Modifier.width(10.dp))
                            }
                        }
                    }
                }
                // 我的话题
                if (state.topicList.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 23.dp)
                            .fillMaxSize()
                            .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                            .background(AppTheme.colors.card)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp, top = 5.dp)
                        ) {
                            Text(
                                text = "我的话题",
                                fontSize = 14.sp,
                                color = AppTheme.colors.textDarkColor
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 15.dp)
                        ) {
                            items(state.topicList) {
                                ProfileTopicCard(date = it.publishTime,
                                    content = it.topicContent,
                                    tags = it.topicTag,
                                    commentCount = it.commentCount,
                                    onDel = { viewModel.dispatch(ProfileViewAction.DelPost(it.topicId)) }) {
                                    AppContext.topicDetail += mapOf(Pair(it.topicId, it))
                                    navController?.navigate("$DETAILS_PAGE/${it.topicId}")
                                }
                                Spacer(modifier = Modifier.height(10.dp))

                            }
                        }
                    }
                } else if (state.resList.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Image(
                                painter = painterResource(id = R.drawable.no_res),
                                contentDescription = "no any resource",
                                modifier = Modifier
                                    .fillMaxHeight(0.5f)
                                    .aspectRatio(1f)
                            )
                            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
                            Text(
                                text = "快和大家分享你的发现吧!",
                                fontSize = 16.sp,
                                color = AppTheme.colors.textLightColor
                            )
                            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .clickable(indication = rememberRipple(),
                                        interactionSource = MutableInteractionSource(),
                                        onClick = {
                                            if (profile.id.isBlank()) navController?.navigate(
                                                AUTH_PAGE
                                            ) { launchSingleTop = true }
                                            else navController?.navigate(PUBLISH_PAGE) {
                                                launchSingleTop = true
                                            }
                                        })
                                    .background(AppTheme.colors.schoolBlue)
                                    .padding(vertical = 10.dp, horizontal = 20.dp)
                            ) {
                                Text(
                                    text = if (profile.id.isBlank()) "去登录" else "去创建话题",
                                    fontSize = 14.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp, end = 5.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(AppTheme.colors.card)
                    .clickable(indication = rememberRipple(),
                        interactionSource = MutableInteractionSource(),
                        onClick = {
                            if (profile.id != "") showSetting = !showSetting
                        }), contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.setting),
                    contentDescription = "setting",
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                )
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        MoreActionCard(
            listOf(ActionPair("清除缓存") {
                // TODO 清除缓存
                // TODO 开发警告
                scaffoldState?.let {
                    popupSnackBar(
                        scope,
                        scaffoldState,
                        SNACK_WARN,
                        "!!!注意!!!该功能正在开发或者测试当中"
                    )
                }
            }, ActionPair("退出登录", true) {
                viewModel.dispatch(ProfileViewAction.Logout)
                showSetting = false
            }), showSetting
        )
    }
    if (showGetPermission.value) {
        if (albumMode) {
            if (Build.VERSION.SDK_INT < 33) GrantPermission(isShow = showGetPermission,
                permission = PermissionType.READ,
                textDenied = "应用程序需要申请数据读取权限来获取相册中的图片",
                textBlock = "应用程序需要申请数据读取权限",
                doAfterPermission = {
                    oldLauncher.launch(Matisse())
                    showGetPermission.value = false
                })
            else {
                newLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                showGetPermission.value = false
            }
        } else {
            GrantPermission(isShow = showGetPermission,
                permission = PermissionType.CAMARA,
                textDenied = "应用程序需要申请相机权限来获取拍摄的图片",
                textBlock = "应用程序需要申请相机权限",
                doAfterPermission = {
                    val file = File.createTempFile(
                        "photo_${System.currentTimeMillis()}",
                        ".png",
                        context.cacheDir
                    )
                    photoUri =
                        FileProvider.getUriForFile(
                            context,
                            "${BuildConfig.APPLICATION_ID}.fileprovider",
                            file
                        )
                    Logger.d(photoUri)
                    photoLauncher.launch(photoUri)
                    showGetPermission.value = false
                })
        }
    }
    BottomDialog(
        items = listOf(BottomButtonItem("拍照") {
            showGetPermission.value = true
            showBottomDialog.value = false
            albumMode = false
        }, BottomButtonItem("从相册中选择") {
            showGetPermission.value = true
            showBottomDialog.value = false
            albumMode = true
        }), isShow = showBottomDialog
    )
    if (isLoading)
        WarpLoadingDialog()

    ProfileEditCard(
        isShow = showEditPage,
        nickname = profile.nickname,
        tags = profile.tags,
        onChange = { nickname, tagString ->
            val tags =
                tagString.split("(\\s)+".toRegex()).toSet().toList().filter { it.isNotBlank() }
                    .subList(0, 2)
            Logger.d("nickname: $nickname, tags: $tags")
            if (nickname != profile.nickname)
                viewModel.dispatch(ProfileViewAction.ChangeNickname(nickname))
            if (tags != profile.tags) {
                viewModel.dispatch(ProfileViewAction.ChangeTags(tags))
            }
        })
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFAFAFA)
private fun ProfilePagePreview() {
    ProfilePage(null, null)
}