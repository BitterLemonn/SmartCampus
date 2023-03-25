package com.lemon.smartcampus.ui.widges

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lemon.smartcampus.ui.theme.AppTheme

@Composable
fun BottomHintDialog(
    hint: String,
    items: List<BottomButtonItem>,
    isShow: MutableState<Boolean>
) {
    if (isShow.value)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.onSurface.copy(alpha = 0.4f))
                .clickable(
                    enabled = isShow.value,
                    indication = null,
                    interactionSource = MutableInteractionSource()
                ) { isShow.value = false }
        )
    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = isShow.value,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = slideIn { fullSize -> IntOffset(0, fullSize.height) },
            exit = slideOut { fullSize -> IntOffset(0, fullSize.height) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                    .background(color = AppTheme.colors.hintLightColor)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                        .background(AppTheme.colors.card)
                        .heightIn(min = 120.dp)
                        .padding(bottom = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = hint,
                        color = AppTheme.colors.textBlackColor,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp, horizontal = 25.dp),
                        textAlign = TextAlign.Center
                    )
                }
                Divider(
                    color = AppTheme.colors.hintLightColor,
                    thickness = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items.forEach {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clickable(
                                    indication = rememberRipple(),
                                    interactionSource = MutableInteractionSource(),
                                    onClick = it.func
                                )
                                .background(AppTheme.colors.card)
                                .padding(vertical = 20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = it.text,
                                fontSize = 14.sp,
                                color = AppTheme.colors.textBlackColor
                            )
                        }
                        if (it != items.last()) {
                            Box(
                                modifier = Modifier
                                    .background(AppTheme.colors.hintLightColor)
                                    .height(12.dp)
                                    .width(1.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
private fun BottomDialogPreview() {
    val list = ArrayList<BottomButtonItem>()
    list.add(BottomButtonItem("确认") {})
    list.add(BottomButtonItem("取消") {})
    BottomHintDialog(
        hint = "应用向你请求权限",
        items = list,
        isShow = remember { mutableStateOf(true) }
    )
}