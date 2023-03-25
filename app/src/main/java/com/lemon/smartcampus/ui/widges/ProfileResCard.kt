package com.lemon.smartcampus.ui.widges

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lemon.smartcampus.R
import com.lemon.smartcampus.ui.theme.AppTheme
import com.lemon.smartcampus.ui.theme.CardDay
import com.lemon.smartcampus.ui.theme.TextLightNight
import java.text.DecimalFormat

@Composable
fun ProfileResCard(
    resType: Int = ResType.UNKNOWN,
    resName: String,
    resSize: Float,
    resLink: String,
    onDownload: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(75.dp)
            .height(110.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(
                interactionSource = MutableInteractionSource(), indication = rememberRipple()
            ) { onClick.invoke() }
            .border(
                width = 1.dp,
                color = AppTheme.colors.hintDarkColor,
                shape = RoundedCornerShape(8.dp)
            ),
        elevation = 10.dp,
        backgroundColor = if (isSystemInDarkTheme()) TextLightNight else CardDay
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 5.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // TODO 替换类型图片
            Box(
                modifier = Modifier
                    .size(25.dp)
                    .clip(CircleShape)
                    .background(AppTheme.colors.hintDarkColor)
            )
            Text(
                text = resName,
                fontSize = 10.sp,
                color = AppTheme.colors.textDarkColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 9.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "${DecimalFormat("0.##").format(resSize)} MB",
                fontSize = 10.sp,
                color = AppTheme.colors.textDarkColor
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomEnd = 8.dp, bottomStart = 8.dp))
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = rememberRipple(),
                        onClick = onClick
                    ),
                contentAlignment = Alignment.TopCenter,
            ) {
                Divider(color = AppTheme.colors.hintLightColor, thickness = 1.dp)
                Image(
                    painter = painterResource(id = R.drawable.download2),
                    contentDescription = "download",
                    modifier = Modifier
                        .padding(vertical = 5.dp)
                        .size(20.dp),
                    colorFilter = if (isSystemInDarkTheme()) ColorFilter.tint(Color.Gray) else null
                )
            }
        }
    }
}

@Composable
@Preview
private fun ProfileResCardPreview() {
    ProfileResCard(resName = "文件名文件名文件名",
        resSize = 100.0f,
        resLink = "",
        onDownload = {},
        onClick = {})
}