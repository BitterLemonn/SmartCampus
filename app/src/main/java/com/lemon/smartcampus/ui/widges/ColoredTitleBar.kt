package com.lemon.smartcampus.ui.widges

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lemon.smartcampus.R
import com.lemon.smartcampus.ui.theme.AppTheme

@Composable
fun ColoredTitleBar(
    color: Color,
    text: String,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
            .background(color),
        contentAlignment = Alignment.CenterStart
    ) {
        Image(
            painter = painterResource(id = R.drawable.back),
            contentDescription = "back",
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .padding(10.dp)
                .clip(CircleShape)
                .clickable(
                    indication = rememberRipple(),
                    interactionSource = MutableInteractionSource(),
                    onClick = onBack
                ),
            colorFilter = if (isSystemInDarkTheme()) ColorFilter.tint(Color.Gray) else null
        )
        Text(
            text = text,
            fontSize = 18.sp,
            color = AppTheme.colors.textBlackColor,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}