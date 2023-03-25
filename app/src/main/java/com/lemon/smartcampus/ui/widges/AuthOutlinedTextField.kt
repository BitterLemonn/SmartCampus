package com.lemon.smartcampus.ui.widges

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lemon.smartcampus.ui.theme.AppTheme
import com.lemon.smartcampus.ui.theme.SchoolRedDay

@Composable
fun AuthOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    enabled:Boolean = true,
    isError:Boolean = false,
    label: @Composable () -> Unit = {},
) {
    OutlinedTextField(value = value,
        onValueChange = onValueChange,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = if (!isSystemInDarkTheme()) Color.White
            else Color(0xFF2F2F2F),
            cursorColor = AppTheme.colors.schoolBlue,
            unfocusedBorderColor = AppTheme.colors.textLightColor,
            focusedBorderColor = AppTheme.colors.schoolBlue,
            focusedLabelColor = AppTheme.colors.schoolBlue,
            disabledBorderColor = Color.Transparent,
            disabledLabelColor = Color.Transparent,
            unfocusedLabelColor = AppTheme.colors.textBlackColor,
            errorBorderColor = AppTheme.colors.schoolRed,
            errorLabelColor = AppTheme.colors.schoolRed
        ),
        shape = RoundedCornerShape(5.dp),
        modifier = modifier,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        maxLines = 1,
        textStyle = TextStyle(
            fontSize = 16.sp,
            lineHeight = 14.sp,
            color = AppTheme.colors.textBlackColor
        ),
        label = label,
        enabled = enabled,
        isError = isError
    )
}