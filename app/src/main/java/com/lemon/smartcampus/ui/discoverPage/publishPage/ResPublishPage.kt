package com.lemon.smartcampus.ui.discoverPage.publishPage

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lemon.smartcampus.ui.theme.AppTheme
import com.lemon.smartcampus.viewModel.topic.publish.PublishViewAction
import com.lemon.smartcampus.viewModel.topic.publish.PublishViewModel

@OptIn(ExperimentalTextApi::class)
@Composable
fun ResPublishPage(
    scaffoldState: ScaffoldState? = null,
    viewModel: PublishViewModel
) {
    val state by viewModel.viewStates.collectAsState()

    TextField(
        value = state.content,
        onValueChange = {
            if (it.length <= 400) viewModel.dispatch(
                PublishViewAction.UpdateContent(it)
            )
        },
        modifier = Modifier
            .fillMaxSize(),
        colors = TextFieldDefaults.textFieldColors(
            textColor = AppTheme.colors.textBlackColor,
            backgroundColor = AppTheme.colors.background,
            cursorColor = AppTheme.colors.schoolBlue,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            placeholderColor = AppTheme.colors.textLightColor
        ),
        textStyle = TextStyle(
            fontSize = 15.sp,
            textMotion = TextMotion.Animated,
            color = AppTheme.colors.textBlackColor
        ),
        shape = RectangleShape,
        placeholder = {
            Text(
                text = "唠嗑唠嗑~\n使用#可创建标签,例如:#标签#\n最多可以创建三个标签,每个标签字数不超过4个",
                fontSize = 14.sp
            )
        },
        maxLines = 20
    )

}

@Composable
@Preview(showBackground = true, backgroundColor = 0XFFFAFAFA)
private fun ResPublishPagePreview() {
    ResPublishPage(viewModel = viewModel())
}