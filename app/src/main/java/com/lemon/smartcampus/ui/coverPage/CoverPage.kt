package com.lemon.smartcampus.ui.coverPage

import android.os.Environment
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
import com.lemon.smartcampus.R
import com.lemon.smartcampus.data.database.database.GlobalDataBase
import com.lemon.smartcampus.data.globalData.AppContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun CoverPage(
    onBack: () -> Unit,
    navToHome: () -> Unit,
) {
    var second by remember { mutableStateOf(2) }
    LaunchedEffect(Unit) {
        flow {
            val profile = GlobalDataBase.database.profileDao().get()
            if (profile != null)
                AppContext.profile = profile
            val setting = GlobalDataBase.database.courseGlobalDao().get()
            if (setting != null)
                AppContext.courseGlobal = setting
            // 读取已下载文件
            val dir =
                File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}/smartCampus")
            val map = AppContext.downloadedFile.toMutableMap()
            dir.list()?.forEach { map[it] = "${dir.absolutePath}/$it" }
            AppContext.downloadedFile = map
            emit("")
        }.onEach {
            while (second != 0) {
                delay(1_000)
                second -= 1
            }
            this.launch(Dispatchers.Main) {
                onBack.invoke()
                navToHome.invoke()
            }
        }.flowOn(Dispatchers.IO).collect()
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
        CoverPage({}, {})
    }
}