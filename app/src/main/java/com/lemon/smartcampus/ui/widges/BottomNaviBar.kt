package com.lemon.smartcampus.ui.widges

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lemon.smartcampus.R
import com.lemon.smartcampus.ui.theme.HintGrayDay
import com.lemon.smartcampus.ui.theme.TextLightDay

@Composable
fun BottomNaviItem(
    @DrawableRes imgRes: Int,
    text: String,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    Box {
        Column(
            modifier = Modifier
                .width(50.dp)
                .height(45.dp)
                .clickable(
                    indication = null,
                    interactionSource = MutableInteractionSource(),
                    onClick = onClick
                ),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = imgRes),
                contentDescription = text,
                modifier = Modifier.size(30.dp)
            )
            Text(text = text, color = TextLightDay, fontSize = 12.sp)
        }
        if (isSelected)
            Canvas(modifier = Modifier.size(25.dp)) {
                val height = size.height
                drawCircle(
                    color = Color(0xFFB9DFFF),
                    radius = height / 2.0f,
                    center = this.center.plus(Offset(20.dp.toPx(), 2.dp.toPx())),
                    blendMode = BlendMode.Multiply,
                )
                drawCircle(
                    color = Color(0xFFB9DFFF),
                    radius = height / 2.0f,
                    center = this.center.plus(Offset(20.dp.toPx(), 2.dp.toPx())),
                    style = Stroke(width = 2f),
                    blendMode = BlendMode.Multiply,
                )
            }
    }
}

@Composable
fun BottomNaviBar(
    modifier: Modifier = Modifier,
    nowSelect: Int = 0,
    onClick: (Int) -> Unit
) {
    Row(
        modifier = modifier
            .background(HintGrayDay)
            .fillMaxWidth()
            .height(54.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        BottomNaviItem(
            imgRes = R.drawable.info,
            text = "资讯",
            isSelected = nowSelect == 0
        ) {
            onClick.invoke(0)
        }
        BottomNaviItem(
            imgRes = R.drawable.discover,
            text = "发现",
            isSelected = nowSelect == 1
        ) {
            onClick.invoke(1)
        }
        BottomNaviItem(
            imgRes = R.drawable.my,
            text = "我的",
            isSelected = nowSelect == 2
        ) {
            onClick.invoke(2)
        }
    }
}

@Composable
@Preview
private fun BottomNaviItemPreview() {
    BottomNaviBar {}
}