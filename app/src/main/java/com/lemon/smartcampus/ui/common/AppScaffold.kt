package com.lemon.smartcampus.ui.common

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.lemon.smartcampus.ui.authPage.AuthPage
import com.lemon.smartcampus.ui.coursePage.CourseEditPage
import com.lemon.smartcampus.ui.coursePage.CourseGlobalPage
import com.lemon.smartcampus.ui.coursePage.CoursePage
import com.lemon.smartcampus.ui.coverPage.CoverPage
import com.lemon.smartcampus.ui.discoverPage.publishPage.PublishPage
import com.lemon.smartcampus.ui.discoverPage.tabPage.TopicDetailPage
import com.lemon.smartcampus.ui.homePage.HomePage
import com.lemon.smartcampus.ui.infoPage.InfoDetail
import com.lemon.smartcampus.ui.infoPage.InfoListPage
import com.lemon.smartcampus.ui.infoPage.InfoType
import com.lemon.smartcampus.ui.memoPage.NotePage
import com.lemon.smartcampus.ui.theme.AppTheme
import com.lemon.smartcampus.ui.toolBtnPage.CalendarPage
import com.lemon.smartcampus.ui.toolBtnPage.CharacterPage
import com.lemon.smartcampus.ui.toolBtnPage.IntroPage
import com.lemon.smartcampus.ui.toolBtnPage.characterPage.CharacterDetailPage
import com.lemon.smartcampus.ui.widges.AppSnackBar
import com.lemon.smartcampus.ui.widges.popupSnackBar
import com.lemon.smartcampus.utils.*
import com.orhanobut.logger.Logger

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppScaffold() {
    val navController = rememberAnimatedNavController()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    fun showToast(msg: String, flag: String) =
        popupSnackBar(scope = scope, scaffoldState = scaffoldState, message = msg, label = flag)

    fun navToAuth() = navController.navigate(AUTH_PAGE) {
        popUpToRoute
        launchSingleTop
    }

    fun navToIntro() = navController.navigate(INTRO_PAGE) { launchSingleTop }
    fun navToCalendarPage() = navController.navigate(CALENDAR_PAGE) { launchSingleTop }
    fun navToCharacterPage() = navController.navigate(CHARACTER_PAGE) { launchSingleTop }
    fun navToCharacterDetailPage(name: String, content: String, imgUrl: String) {
        val newImgUrl = imgUrl.replace("/", "　")
        navController.navigate("$CHARACTER_DETAIL_PAGE/$name/$newImgUrl?content=$content") { launchSingleTop }
    }

    fun navToHome() = navController.navigate(HOME_PAGE) { launchSingleTop }
    fun navToPublish() = navController.navigate(PUBLISH_PAGE) { launchSingleTop }
    fun navToPostDetail(id: String) =
        navController.navigate("$DETAILS_PAGE/$id") { launchSingleTop }

    fun forceToAuth() {
        navController.backQueue.clear()
        navToHome()
        navToAuth()
    }

    fun navToCourse() = navController.navigate(COURSE_PAGE) { launchSingleTop }
    fun navToCourseGlobal() = navController.navigate(COURSE_GLOBAL_PAGE) { launchSingleTop }
    fun navToCourseEdit(id: String) =
        navController.navigate("$COURSE_EDIT_PAGE?courseID={$id}") { launchSingleTop }

    fun navToNote() = navController.navigate(NOTE_PAGE) { launchSingleTop }
    fun navToInfoList(type: Int) = navController.navigate("$INFO_LIST/$type") { launchSingleTop }
    fun navToInfoDetail(id: String) = navController.navigate("$INFO_DETAIL/$id") { launchSingleTop }
    fun onBack() = navController.popBackStack()

    Scaffold(modifier = Modifier
        .statusBarsPadding()
        .navigationBarsPadding(), snackbarHost = {
        SnackbarHost(
            hostState = scaffoldState.snackbarHostState
        ) { data ->
            AppSnackBar(data = data)
        }
    }) { padding ->
        AnimatedNavHost(
            modifier = Modifier
                .background(color = AppTheme.colors.background)
                .fillMaxSize()
                .padding(padding), navController = navController, startDestination = COVER_PAGE
        ) {
            composable(route = COVER_PAGE) {
                rememberSystemUiController().setSystemBarsColor(
                    if (!isSystemInDarkTheme()) Color(0xFF007AFF)
                    else Color(0xFF012E5F), darkIcons = isSystemInDarkTheme()
                )
                CoverPage(onBack = { onBack() }, navToHome = { navToHome() })
            }
            composable(route = HOME_PAGE) {
                rememberSystemUiController().setNavigationBarColor(
                    if (isSystemInDarkTheme()) AppTheme.colors.hintDarkColor else Color.White,
                    darkIcons = isSystemInDarkTheme()
                )
                rememberSystemUiController().setStatusBarColor(
                    AppTheme.colors.background, darkIcons = !isSystemInDarkTheme()
                )
                HomePage(showToast = { msg, flag -> showToast(msg, flag) },
                    navToAuth = { navToAuth() },
                    navToPublish = { navToPublish() },
                    navToPostDetail = { navToPostDetail(it) },
                    navToCourse = { navToCourse() },
                    navToNote = { navToNote() },
                    navToInfoList = { navToInfoList(it) },
                    navToInfoDetail = { navToInfoDetail(it) },
                    navToIntro = { navToIntro() },
                    navToCalendarPage = { navToCalendarPage() },
                    navToCharacter = { navToCharacterPage() },
                    forceToAuth = { forceToAuth() })
            }
            composable(route = INTRO_PAGE) {
                rememberSystemUiController().setSystemBarsColor(
                    AppTheme.colors.background, darkIcons = isSystemInDarkTheme()
                )
                IntroPage(onBack = { onBack() })
            }
            composable(route = CALENDAR_PAGE) {
                rememberSystemUiController().setSystemBarsColor(
                    AppTheme.colors.background, darkIcons = isSystemInDarkTheme()
                )
                CalendarPage(onBack = { onBack() })
            }
            composable(route = CHARACTER_PAGE) {
                rememberSystemUiController().setSystemBarsColor(
                    AppTheme.colors.background, darkIcons = !isSystemInDarkTheme()
                )
                CharacterPage(showToast = { msg, flag -> showToast(msg, flag) },
                    onBack = { onBack() },
                    { name, content, imgUrl ->
                        navToCharacterDetailPage(
                            name = name,
                            content = content,
                            imgUrl = imgUrl
                        )
                    })
            }
            composable(
                route = "$CHARACTER_DETAIL_PAGE/{name}/{imgUrl}?content={content}",
                arguments = listOf(
                    navArgument("name") { defaultValue = "" },
                    navArgument("content") {
                        defaultValue = ""
                        nullable = true
                    },
                    navArgument("imgUrl") { defaultValue = "" }
                ),
            ) {
                rememberSystemUiController().setSystemBarsColor(
                    AppTheme.colors.background, darkIcons = !isSystemInDarkTheme()
                )
                val argument = it.arguments
                val name = argument?.getString("name") ?: ""
                val content = argument?.getString("content") ?: ""
                val imgUrl = (argument?.getString("imgUrl") ?: "").replace("　", "/")
                Logger.d("imgUrl: $imgUrl")
                CharacterDetailPage(name = name, content = content, imgUrl = imgUrl)
            }
            composable(route = CHARACTER_DETAIL_PAGE) {
                rememberSystemUiController().setSystemBarsColor(
                    AppTheme.colors.background, darkIcons = !isSystemInDarkTheme()
                )
                CharacterDetailPage(name = "name", content = "content", imgUrl = "imgUrl")
            }
            composable(route = AUTH_PAGE) {
                rememberSystemUiController().setSystemBarsColor(
                    AppTheme.colors.background, darkIcons = isSystemInDarkTheme()
                )
                AuthPage(showToast = { msg, flag -> showToast(msg, flag) }, onBack = { onBack() })
            }
            composable(route = PUBLISH_PAGE, enterTransition = {
                expandIn(
                    expandFrom = Alignment.TopCenter, animationSpec = tween(400)
                ) { IntSize.Zero } + fadeIn()
            }, popEnterTransition = {
                expandIn(
                    expandFrom = Alignment.TopCenter, animationSpec = tween(400)
                ) { IntSize.Zero } + fadeIn()
            }, popExitTransition = {
                shrinkOut(
                    shrinkTowards = Alignment.TopCenter, animationSpec = tween(400)
                ) { IntSize.Zero } + fadeOut()
            }, exitTransition = {
                shrinkOut(
                    shrinkTowards = Alignment.TopCenter, animationSpec = tween(400)
                ) { IntSize.Zero } + fadeOut()
            }) {
                rememberSystemUiController().setSystemBarsColor(
                    AppTheme.colors.background, darkIcons = isSystemInDarkTheme()
                )
                PublishPage(scaffoldState = scaffoldState,
                    navToAuth = { navToAuth() },
                    onBack = { onBack() })
            }
            composable(route = "$DETAILS_PAGE/{id}",
                arguments = listOf(navArgument("id") { type = NavType.StringType }),
                enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                exitTransition = { slideOutHorizontally(targetOffsetX = { it }) }) {
                rememberSystemUiController().setNavigationBarColor(
                    AppTheme.colors.background, darkIcons = isSystemInDarkTheme()
                )
                val argument = it.arguments
                val id = argument?.getString("id") ?: ""
                TopicDetailPage(scaffoldState = scaffoldState, id = id, onBack = { onBack() })
            }
            composable(route = COURSE_PAGE) {
                rememberSystemUiController().setStatusBarColor(
                    if (!isSystemInDarkTheme()) Color(0xFFFFF3D8) else Color(0xFF575249),
                    darkIcons = isSystemInDarkTheme()
                )
                CoursePage(scaffoldState = scaffoldState,
                    onBack = { onBack() },
                    navToCourseEdit = { navToCourseEdit(it) })
                rememberSystemUiController().setNavigationBarColor(
                    AppTheme.colors.background, darkIcons = isSystemInDarkTheme()
                )
            }
            composable(route = "$COURSE_EDIT_PAGE?courseID={courseID}",
                arguments = listOf(navArgument("courseID") {
                    type = NavType.StringType
                    defaultValue = ""
                }),
                enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                exitTransition = { slideOutHorizontally(targetOffsetX = { it }) }) {
                CourseEditPage(scaffoldState = scaffoldState,
                    courseID = it.arguments?.getString("courseID"),
                    onBack = { onBack() },
                    navToCourseGlobal = { navToCourseGlobal() })
            }
            composable(route = COURSE_GLOBAL_PAGE,
                enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                exitTransition = { slideOutHorizontally(targetOffsetX = { it }) }) {
                CourseGlobalPage(scaffoldState = scaffoldState, onBack = { onBack() })
            }
            composable(
                route = "$INFO_DETAIL/{infoID}",
                enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                exitTransition = { slideOutHorizontally(targetOffsetX = { it }) },
                arguments = listOf(navArgument("infoID") {
                    type = NavType.StringType
                    defaultValue = ""
                })
            ) {
                InfoDetail(
                    navController = navController,
                    scaffoldState = scaffoldState,
                    infoID = it.arguments?.getString("infoID") ?: ""
                )
            }
            composable(
                route = "$INFO_LIST/{type}",
                enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                exitTransition = { slideOutHorizontally(targetOffsetX = { it }) },
                arguments = listOf(navArgument("type") {
                    type = NavType.IntType
                    defaultValue = InfoType.NEWS
                })
            ) { argument ->
                InfoListPage(scaffoldState = scaffoldState,
                    type = argument.arguments?.getInt("type") ?: InfoType.NEWS,
                    onBack = { onBack() },
                    navToInfoDetail = { navToInfoDetail(it) })
            }
            composable(route = NOTE_PAGE) {
                NotePage(navController = navController, scaffoldState = scaffoldState)
                rememberSystemUiController().setStatusBarColor(
                    if (!isSystemInDarkTheme()) Color(0xFFD0F2FF)
                    else Color(0xFF6D7F86), darkIcons = !isSystemInDarkTheme()
                )
                rememberSystemUiController().setNavigationBarColor(
                    color = AppTheme.colors.background, darkIcons = !isSystemInDarkTheme()
                )
            }
        }
    }
}