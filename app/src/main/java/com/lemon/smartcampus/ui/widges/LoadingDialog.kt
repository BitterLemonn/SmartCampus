package com.lemon.smartcampus.ui.widges

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.lemon.smartcampus.R
import com.lemon.smartcampus.ui.theme.AppTheme

@Composable
private fun LoadingDialog(
    text: String? = null,
    dialogSize: Int,
) {
    val isNight = isSystemInDarkTheme()

    Canvas(modifier = Modifier.size(dialogSize.dp)) {
        inset {
            drawRoundRect(
                color = if (isNight) Color.Black else Color.White,
                size = size,
                cornerRadius = CornerRadius(20.0f),
            )
        }
    }
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .size(dialogSize.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(if (isNight) R.drawable.loading2 else R.drawable.loading3)
                .decoderFactory(GifDecoder.Factory())
                .build(),
            contentDescription = "loading picture",
            modifier = Modifier
                .clip(CircleShape)
                .size(90.dp)
        )
        if (text != null)
            Text(
                text = text,
                fontSize = 14.sp,
                color = Color.Gray
            )
    }
}

@Composable
fun WarpLoadingDialog(
    text: String? = null,
    size: Int = 120,
    backgroundAlpha: Float = 0.4f,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = AppTheme.colors.textLightColor.copy(alpha = backgroundAlpha)
    ) {
        Box(modifier = Modifier.wrapContentSize()) {
            LoadingDialog(if (text != null) "$text..." else null, size)
        }
    }
}

@Composable
@Preview
private fun LoadingPreview() {
    WarpLoadingDialog("正在登录", 120)
}