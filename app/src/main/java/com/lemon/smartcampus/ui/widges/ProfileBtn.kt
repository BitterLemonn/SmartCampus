package com.lemon.smartcampus.ui.widges

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lemon.smartcampus.R
import com.lemon.smartcampus.ui.theme.AppTheme

@Composable
fun ProfileBtn(
    @DrawableRes img: Int,
    text: String,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(144.dp),
        elevation = 10.dp,
        shape = RoundedCornerShape(8.dp),
        backgroundColor = backgroundColor
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    indication = rememberRipple(),
                    interactionSource = MutableInteractionSource(),
                    onClick = onClick
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 18.dp)
                    .padding(start = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = img),
                    contentDescription = text,
                    modifier = Modifier.size(25.dp)
                )
                Spacer(modifier = Modifier.width(13.dp))
                Text(text = text, fontSize = 14.sp, color = AppTheme.colors.textDarkColor)
            }
        }
    }
}

@Composable
@Preview
private fun ProfileBtnPreview() {
    ProfileBtn(img = R.drawable.calendar2, text = "我的课程", backgroundColor = Color(0xFFFFF3D8)) {}
}