package com.lemon.smartcampus.ui.widges

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lemon.smartcampus.R
import com.lemon.smartcampus.ui.theme.HintGrayDay
import com.lemon.smartcampus.ui.theme.TextLightDay

@Composable
fun HomePageTitle(
    title: String,
    titleEn: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .height(30.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = onClick
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(text = title, fontSize = 18.sp, color = Color.Black)
            Text(
                text = titleEn,
                fontSize = 12.sp,
                color = TextLightDay,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp)
            )
            Text(text = "more", fontSize = 12.sp, color = TextLightDay)
            Image(
                painter = painterResource(id = R.drawable.right_more),
                contentDescription = "more",
                modifier = Modifier.size(12.dp).padding(bottom = 1.dp)
            )
        }
        Divider(color = HintGrayDay, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
@Preview
fun HomePageTitlePreview() {
    HomePageTitle(title = "广外新闻", titleEn = "News") {

    }
}