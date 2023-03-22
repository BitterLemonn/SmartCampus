package com.lemon.smartcampus.ui.widges

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lemon.smartcampus.R
import com.lemon.smartcampus.ui.theme.AppTheme

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    key: String,
    onKeyChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(30.dp)
            .border(
                border = BorderStroke(1.dp, AppTheme.colors.textLightColor),
                RoundedCornerShape(10.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .matchParentSize()
                .padding(vertical = 2.dp, horizontal = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = key,
                onValueChange = onKeyChange,
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = AppTheme.colors.textLightColor,
                    backgroundColor = Color.Transparent,
                    cursorColor = AppTheme.colors.textDarkColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onSearch.invoke() }
                ),
                modifier = Modifier.fillMaxWidth(0.9f)
            )
            Image(
                painter = painterResource(id = R.drawable.search),
                contentDescription = "search button",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null,
                        onClick = onSearch
                    )
                    .padding(vertical = 2.dp)
            )
        }
    }
}

@Composable
@Preview
private fun SearchBarPreview() {
    SearchBar(key = "", onKeyChange = {}) {}
}