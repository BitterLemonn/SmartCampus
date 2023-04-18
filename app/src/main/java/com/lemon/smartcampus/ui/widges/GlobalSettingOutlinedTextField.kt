package com.lemon.smartcampus.ui.widges

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lemon.smartcampus.ui.theme.AppTheme

@Composable
fun GlobalSettingOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    @DrawableRes leadingIcon: Int? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    placeholder: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Number,
        imeAction = ImeAction.Next
    ),
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        leadingIcon = {
            if (leadingIcon != null)
                Image(
                    painter = painterResource(id = leadingIcon),
                    contentDescription = placeholder,
                    modifier = Modifier.size(25.dp),
                    colorFilter = if (isSystemInDarkTheme()) ColorFilter.tint(Color.Gray) else null
                )
        },
        trailingIcon = trailingIcon,
        label = {
            Text(text = placeholder, fontSize = 14.sp, color = AppTheme.colors.textDarkColor)
        },
        shape = RoundedCornerShape(10.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = AppTheme.colors.textBlackColor,
            backgroundColor = AppTheme.colors.hintLightColor,
            cursorColor = AppTheme.colors.schoolBlue,
            unfocusedBorderColor = AppTheme.colors.hintDarkColor,
            focusedBorderColor = AppTheme.colors.schoolBlue
        ),
        textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 16.sp),
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
    )
}