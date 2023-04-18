package com.lemon.smartcampus.ui.coverPage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.lemon.smartcampus.R
import com.lemon.smartcampus.data.database.database.GlobalDataBase
import com.lemon.smartcampus.data.globalData.AppContext
import com.lemon.smartcampus.utils.HOME_PAGE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CoverPage(
    navController: NavController?
) {
    var second by remember { mutableStateOf(3) }
    LaunchedEffect(Unit) {
        this.launch(Dispatchers.IO) {
            val profile = GlobalDataBase.database.profileDao().get()
            if (profile != null)
                AppContext.profile = profile
            val setting = GlobalDataBase.database.courseGlobalDao().get()
            if (setting != null)
                AppContext.courseGlobal = setting
        }
        while (second != 0) {
            delay(1_000)
            second -= 1
        }
        navController?.popBackStack()
        navController?.navigate(HOME_PAGE) {
            popUpToRoute
            launchSingleTop = true
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (!isSystemInDarkTheme()) Color(0xFF007AFF) else Color(0xFF012E5F))
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "logo",
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .align(Alignment.Center),
            colorFilter = if (isSystemInDarkTheme()) ColorFilter.tint(
                Color.Gray,
                BlendMode.Multiply
            ) else null
        )
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
private fun CoverPagePreview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF007AFF)
    ) {
        CoverPage(navController = null)
    }
}