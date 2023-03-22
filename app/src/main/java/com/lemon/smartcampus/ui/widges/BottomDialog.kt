package com.lemon.smartcampus.ui.widges

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lemon.smartcampus.ui.theme.AppTheme

@Composable
fun BottomDialog(
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                    .background(color = AppTheme.colors.hintLightColor)
            ) {
                items(items) { item ->
                    BottomDialogButton(onClick = item.func, text = item.text)
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        color = AppTheme.colors.hintLightColor,
                        thickness = (0.5).dp
                    )
                    if (item == items.last())
                        Spacer(Modifier.height(10.dp))
                }
                item {
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        color = AppTheme.colors.hintLightColor,
                        thickness = (0.5).dp
                    )
                    BottomDialogButton(onClick = { isShow.value = false }, text = "取消")
                }
            }
        }
    }
}

@Composable
fun BottomDialogButton(onClick: () -> Unit = {}, text: String) {
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = AppTheme.colors.card),
        contentPadding = PaddingValues(vertical = 20.dp),
        shape = RectangleShape
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            color = AppTheme.colors.textBlackColor
        )
    }
}

@Composable
@Preview(showSystemUi = true)
private fun BottomDialogPreview() {
    val list = ArrayList<BottomButtonItem>()
    list.add(BottomButtonItem("拍照") {})
    list.add(BottomButtonItem("从相册中选择") {})
    BottomDialog(items = list, isShow = remember { mutableStateOf(true) })
}

data class BottomButtonItem(
    val text: String,
    val func: () -> Unit
)