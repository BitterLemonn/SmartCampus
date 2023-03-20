package com.lemon.smartcampus.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.lemon.smartcampus.common.AUTH_PAGE
import com.lemon.smartcampus.common.COVER_PAGE
import com.lemon.smartcampus.common.HOME_PAGE
import com.lemon.smartcampus.ui.authPage.AuthPage
import com.lemon.smartcampus.ui.coverPage.CoverPage
import com.lemon.smartcampus.ui.homePage.HomePage
import com.lemon.smartcampus.ui.theme.HintGrayDay

@Composable
fun AppScaffold() {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
    ) { padding ->
        NavHost(
            modifier = Modifier
                .background(color = Color(0xFFFAFAFA))
                .fillMaxSize()
                .padding(padding),
            navController = navController,
            startDestination = COVER_PAGE
        ) {
            composable(route = COVER_PAGE) {
                rememberSystemUiController().setNavigationBarColor(
                    Color(0xFF007AFF),
                    darkIcons = MaterialTheme.colors.isLight
                )
                rememberSystemUiController().setSystemBarsColor(
                    Color(0xFF007AFF),
                    darkIcons = MaterialTheme.colors.isLight
                )
                CoverPage(navController = navController)
            }
            composable(route = HOME_PAGE) {
                rememberSystemUiController().setNavigationBarColor(
                    Color(0xFFFAFAFA), darkIcons = MaterialTheme.colors.isLight
                )
                rememberSystemUiController().setSystemBarsColor(
                    HintGrayDay,
                    darkIcons = MaterialTheme.colors.isLight
                )
                HomePage(navController = navController)
            }
            composable(route = AUTH_PAGE) {
                rememberSystemUiController().setNavigationBarColor(
                    Color(0xFFFAFAFA), darkIcons = MaterialTheme.colors.isLight
                )
                rememberSystemUiController().setSystemBarsColor(
                    HintGrayDay,
                    darkIcons = MaterialTheme.colors.isLight
                )
                AuthPage(navController = navController)
            }
        }
    }
}