package com.lemon.smartcampus.ui.widges

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ActionPair(
    val text: String, val action: () -> Unit
)

@Composable
fun MoreActionCard(
    actionList: List<ActionPair>, isShow: Boolean = false
) {
    val isShowRemember by rememberUpdatedState(newValue = isShow)
    val widthAnimate by animateDpAsState(targetValue = if (isShowRemember) 120.dp else 0.dp)
    val heightAnimate by animateDpAsState(targetValue = if (isShowRemember) Dp.Unspecified else 0.dp)

    Card(
        modifier = Modifier
            .width(widthAnimate)
            .height(heightAnimate),
        elevation = 10.dp,
        shape = RoundedCornerShape(10.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            itemsIndexed(actionList) { index, item ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(
                            RoundedCornerShape(
                                topEnd = if (index == 0) 10.dp else 0.dp,
                                topStart = if (index == 0) 10.dp else 0.dp,
                                bottomStart = if (index == actionList.lastIndex) 10.dp else 0.dp,
                                bottomEnd = if (index == actionList.lastIndex) 10.dp else 0.dp
                            )
                        )
                        .clickable(
                            indication = rememberRipple(),
                            interactionSource = MutableInteractionSource(),
                            onClick = item.action
                        )
                        .padding(vertical = 7.dp, horizontal = 13.dp)
                ) {
                    Text(
                        text = item.text,
                        fontSize = 14.sp,
                        maxLines = 1,
                        modifier = Modifier.padding(vertical = 5.dp)
                    )
                }
            }
        }
    }
}