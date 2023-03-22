package com.lemon.smartcampus.ui.common

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.lemon.smartcampus.ui.authPage.AuthPage
import com.lemon.smartcampus.ui.coverPage.CoverPage
import com.lemon.smartcampus.ui.homePage.HomePage
import com.lemon.smartcampus.ui.theme.AppTheme
import com.lemon.smartcampus.ui.widges.AppSnackBar
import com.lemon.smartcampus.utils.AUTH_PAGE
import com.lemon.smartcampus.utils.COVER_PAGE
import com.lemon.smartcampus.utils.HOME_PAGE

@Composable
fun AppScaffold() {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        snackbarHost = {
            SnackbarHost(
                hostState = scaffoldState.snackbarHostState
            ) { data ->
                AppSnackBar(data = data)
            }
        }
    ) { padding ->
        NavHost(
            modifier = Modifier
                .background(
                    color = AppTheme.colors.background
                )
                .fillMaxSize()
                .padding(padding),
            navController = navController,
            startDestination = COVER_PAGE
        ) {
            composable(route = COVER_PAGE) {
                rememberSystemUiController().setSystemBarsColor(
                    if (!isSystemInDarkTheme()) Color(0xFF007AFF)
                    else Color(0xFF012E5F),
                    darkIcons = isSystemInDarkTheme()
                )
                CoverPage(navController = navController)
            }
            composable(route = HOME_PAGE) {
                rememberSystemUiController().setNavigationBarColor(
                    if (isSystemInDarkTheme()) AppTheme.colors.textDarkColor else Color.White,
                    darkIcons = isSystemInDarkTheme()
                )
                rememberSystemUiController().setStatusBarColor(
                    if (!isSystemInDarkTheme()) AppTheme.colors.background
                    else Color.Black,
                    darkIcons = isSystemInDarkTheme()
                )
                HomePage(navController = navController, scaffoldState = scaffoldState)
            }
            composable(route = AUTH_PAGE) {
                rememberSystemUiController().setSystemBarsColor(
                    if (!isSystemInDarkTheme()) AppTheme.colors.background
                    else Color.Black, darkIcons = isSystemInDarkTheme()
                )
                AuthPage(navController = navController, scaffoldState = scaffoldState)
            }
        }
    }
}