package com.lemon.smartcampus.ui.widges

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lemon.smartcampus.ui.theme.AppTheme

@Composable
fun TabTitle(
    tabList: List<String>,
    nowSelect: Int = 0,
    onClick: (Int) -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.BottomStart) {
        TabRow(
            selectedTabIndex = nowSelect,
            modifier = Modifier
                .padding(start = 20.dp)
                .fillMaxWidth(0.5f),
            backgroundColor = Color.Transparent,
            indicator = {
                TabRowDefaults.Indicator(
                    modifier = Modifier
                        .customTabIndicatorOffset(it[nowSelect], 0.4f)
                        .width(10.dp)
                        .clip(RoundedCornerShape(1.dp)),
                    color = AppTheme.colors.schoolBlue,
                    height = 2.dp
                )
            }
        ) {
            tabList.forEachIndexed { index, title ->
                Tab(
                    selected = index == nowSelect,
                    onClick = {
                        onClick.invoke(index)
                    },
                    text = {
                        Text(
                            color = AppTheme.colors.textBlackColor,
                            text = title,
                            fontSize = (18 - title.length).sp,
                            modifier = Modifier.widthIn(min = (title.length * 20).dp)
                        )
                    },
                    modifier = Modifier.clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                )
            }
        }
        Divider(
            thickness = 1.dp,
            color = AppTheme.colors.hintLightColor,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
@Preview
private fun TabTitlePreview() {
    TabTitle(tabList = listOf("话题", "资源")) {}
}

private fun Modifier.customTabIndicatorOffset(
    currentTabPosition: TabPosition,
    percentLength: Float
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "tabIndicatorOffset"
        value = currentTabPosition
    }
) {
    val currentTabWidth by animateDpAsState(
        targetValue = currentTabPosition.width,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
    )
    val indicatorOffset by animateDpAsState(
        targetValue = currentTabPosition.left,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
    )
    val indicatorLength = currentTabWidth * percentLength
    fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset(
            x = indicatorOffset + currentTabWidth / 2 - indicatorLength / 2,
            (-10).dp
        )  // 偏移量加上宽度的1/4
        .width(indicatorLength)
}