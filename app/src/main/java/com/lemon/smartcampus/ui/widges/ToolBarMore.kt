package com.lemon.smartcampus.ui.widges

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lemon.smartcampus.R

@Composable
fun ToolBarMore(
    rightIcon: (@Composable () -> Unit)? = null,
    onMore: (() -> Unit)? = null,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 5.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.back),
            contentDescription = "back",
            modifier = Modifier
                .clip(CircleShape)
                .clickable(
                    indication = rememberRipple(),
                    interactionSource = MutableInteractionSource(),
                    onClick = onBack
                )
                .padding(5.dp)
                .size(20.dp)
        )
        if (rightIcon == null && onMore != null) {
            Image(
                painter = painterResource(id = R.drawable.more),
                contentDescription = "more",
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable(
                        indication = rememberRipple(),
                        interactionSource = MutableInteractionSource(),
                        onClick = onMore
                    )
                    .size(30.dp)
            )
        } else rightIcon?.invoke()
    }
}

@Composable
@Preview
private fun ToolBarPreview() {
    ToolBarMore(onMore = {}) {}
}