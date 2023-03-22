package com.lemon.smartcampus.ui.widges

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lemon.smartcampus.ui.theme.AppTheme

@Composable
fun HintDialog(
    hint: String = "",
    onClickCertain: () -> Unit = {},
    onClickCancel: () -> Unit = {},
    highLineCertain: Boolean = true
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.onSurface.copy(alpha = 0.1f))
            .clickable(interactionSource = MutableInteractionSource(),
                indication = null,
                onClick = {}),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(AppTheme.colors.card)
                .sizeIn(
                    minWidth = 250.dp,
                    minHeight = 120.dp,
                    maxWidth = 450.dp,
                    maxHeight = 200.dp
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .matchParentSize()
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentSize()
                ) {
                    Text(
                        text = hint,
                        fontSize = 16.sp,
                        color = AppTheme.colors.textDarkColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 10.dp)
                    )
                }
                Divider(
                    color = AppTheme.colors.hintLightColor,
                    thickness = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = onClickCertain,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp)
                    ) {
                        Text(
                            text = "确定",
                            color = if (highLineCertain) AppTheme.colors.schoolBlue
                            else AppTheme.colors.textLightColor,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .background(Color.Gray.copy(alpha = 0.3f))
                            .fillMaxHeight()
                            .padding(bottom = 5.dp)
                    )
                    Button(
                        onClick = onClickCancel,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp)
                    ) {
                        Text(
                            text = "取消",
                            color = if (highLineCertain) AppTheme.colors.textLightColor
                            else AppTheme.colors.schoolRed,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
private fun HintDialogPreview() {
    HintDialog(
        hint = "12312312312123123123123232",
        highLineCertain = false
    )
}