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
import com.lemon.smartcampus.ui.course.CourseEditPage
import com.lemon.smartcampus.ui.course.CoursePage
import com.lemon.smartcampus.ui.coverPage.CoverPage
import com.lemon.smartcampus.ui.discoverPage.publishPage.PublishPage
import com.lemon.smartcampus.ui.discoverPage.tabPage.TopicDetailPage
import com.lemon.smartcampus.ui.homePage.HomePage
import com.lemon.smartcampus.ui.theme.AppTheme
import com.lemon.smartcampus.ui.widges.AppSnackBar
import com.lemon.smartcampus.utils.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppScaffold() {
    val navController = rememberAnimatedNavController()
    val scaffoldState = rememberScaffoldState()

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
                .padding(padding),
            navController = navController,
            startDestination = COVER_PAGE
        ) {
            composable(route = COVER_PAGE) {
                rememberSystemUiController().setSystemBarsColor(
                    if (!isSystemInDarkTheme()) Color(0xFF007AFF)
                    else Color(0xFF012E5F), darkIcons = isSystemInDarkTheme()
                )
                CoverPage(navController = navController)
            }
            composable(route = HOME_PAGE) {
                rememberSystemUiController().setNavigationBarColor(
                    if (isSystemInDarkTheme()) AppTheme.colors.hintDarkColor else Color.White,
                    darkIcons = isSystemInDarkTheme()
                )
                rememberSystemUiController().setStatusBarColor(
                    AppTheme.colors.background, darkIcons = !isSystemInDarkTheme()
                )
                HomePage(navController = navController, scaffoldState = scaffoldState)
            }
            composable(route = AUTH_PAGE) {
                rememberSystemUiController().setSystemBarsColor(
                    AppTheme.colors.background, darkIcons = isSystemInDarkTheme()
                )
                AuthPage(navController = navController, scaffoldState = scaffoldState)
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
                PublishPage(navController = navController, scaffoldState = scaffoldState)
            }
            composable(route = "$DETAILS_PAGE/{id}",
                arguments = listOf(navArgument("id") { type = NavType.StringType }),
                enterTransition = {
                    expandIn(
                        expandFrom = Alignment.TopCenter, animationSpec = tween(200)
                    ) { IntSize.Zero } + fadeIn()
                },
                popEnterTransition = {
                    expandIn(
                        expandFrom = Alignment.TopCenter, animationSpec = tween(200)
                    ) { IntSize.Zero } + fadeIn()
                },
                popExitTransition = {
                    shrinkOut(
                        shrinkTowards = Alignment.TopCenter, animationSpec = tween(200)
                    ) { IntSize.Zero } + fadeOut()
                },
                exitTransition = {
                    shrinkOut(
                        shrinkTowards = Alignment.TopCenter, animationSpec = tween(200)
                    ) { IntSize.Zero } + fadeOut()
                }) {
                rememberSystemUiController().setNavigationBarColor(
                    AppTheme.colors.background, darkIcons = isSystemInDarkTheme()
                )
                val argument = it.arguments
                val id = argument?.getString("id") ?: ""
                TopicDetailPage(
                    navController = navController, scaffoldState = scaffoldState, id = id
                )
            }
            composable(route = COURSE_PAGE) {
                rememberSystemUiController().setStatusBarColor(
                    if (!isSystemInDarkTheme()) Color(0xFFFFF3D8) else Color(0xFF575249),
                    darkIcons = isSystemInDarkTheme()
                )
                CoursePage(navController = navController, scaffoldState)
                rememberSystemUiController().setNavigationBarColor(
                    AppTheme.colors.background, darkIcons = isSystemInDarkTheme()
                )
            }
            composable(route = COURSE_EDIT_PAGE,
                enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
            ) {
                CourseEditPage(navController = navController, scaffoldState = scaffoldState)
            }
        }
    }
}