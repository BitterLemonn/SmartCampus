package com.lemon.smartcampus.ui.widges

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lemon.smartcampus.ui.theme.AppTheme

@Composable
fun IntroduceTitle(
    titleCN: String,
    titleEN: String
) {
    Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.width(if (titleCN.length < 5) 80.dp else 180.dp)) {
            Text(
                text = titleCN,
                color = AppTheme.colors.textBlackColor,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Row(verticalAlignment = Alignment.Top) {
                Spacer(modifier = Modifier.width(10.dp))
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .background(AppTheme.colors.schoolRed)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .background(AppTheme.colors.schoolBlue)
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
        }
        Text(text = titleEN, color = AppTheme.colors.textDarkColor, fontSize = 12.sp)
    }
}

@Composable
@Preview
private fun IntroduceTitlePreview() {
    IntroduceTitle(
        "学校简介",
        "Intro of GDUFS"
    )
}