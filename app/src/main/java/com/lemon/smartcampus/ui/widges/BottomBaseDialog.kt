package com.lemon.smartcampus.ui.widges

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lemon.smartcampus.ui.theme.AppTheme

@Composable
fun BottomBaseDialog(
    content: @Composable () -> Unit,
    items: List<BottomButtonItem>,
    isShow: MutableState<Boolean>
) {
    if (isShow.value)
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colors.onSurface.copy(alpha = 0.4f))
                .fillMaxSize()
        )
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
        if (isShow.value)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clickable(
                        enabled = isShow.value,
                        indication = null,
                        interactionSource = MutableInteractionSource()
                    ) { isShow.value = false }
            )
        else Box(modifier = Modifier.weight(1f))
        AnimatedVisibility(
            visible = isShow.value,
            enter = slideInVertically { it },
            exit = slideOutVertically { it }
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
                    content.invoke()
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