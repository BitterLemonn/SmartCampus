package com.lemon.smartcampus.ui.widges

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lemon.smartcampus.ui.theme.AppTheme

@Composable
fun CourseTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle.Default,
    leadingIcon: (@Composable () -> Unit)? = null,
    label: (@Composable () -> Unit)? = null,
    enable: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = AppTheme.colors.textBlackColor,
            backgroundColor = AppTheme.colors.background,
            cursorColor = AppTheme.colors.schoolBlue,
            focusedBorderColor = AppTheme.colors.hintDarkColor,
            unfocusedBorderColor = AppTheme.colors.hintLightColor,
            disabledBorderColor = AppTheme.colors.hintLightColor,
            disabledTextColor = AppTheme.colors.textBlackColor
        ),
        textStyle = textStyle,
        singleLine = true,
        leadingIcon = leadingIcon,
        label = label,
        enabled = enable
    )
}