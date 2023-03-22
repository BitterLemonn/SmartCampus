package com.lemon.smartcampus.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = AppColors(
    textLightColor = TextLightNight,
    textDarkColor = TextDarkNight,
    textBlackColor = TextBlackNight,
    hintDarkColor = HintDarkNight,
    hintLightColor = HintLightNight,
    card = CardNight,
    background = BackgroundNight,
    schoolBlue = SchoolBlueNight,
    schoolRed = SchoolRedNight,
    info = InfoNight,
    warn = WarnNight,
    success = SuccessNight,
    error = ErrorNight
)

private val LightColorPalette = AppColors(
    textLightColor = TextLightDay,
    textDarkColor = TextDarkDay,
    textBlackColor = TextBlackDay,
    hintDarkColor = HintDarkDay,
    hintLightColor = HintLightDay,
    card = CardDay,
    background = BackgroundDay,
    schoolBlue = SchoolBlueDay,
    schoolRed = SchoolRedDay,
    info = InfoDay,
    warn = WarnDay,
    success = SuccessDay,
    error = ErrorDay
)

var LocalAppColors = compositionLocalOf {
    LightColorPalette
    DarkColorPalette
}

@Stable
object AppTheme {
    val colors: AppColors
        @Composable
        get() = LocalAppColors.current

    enum class Theme {
        Light, Dark
    }
}

class AppColors(
    textLightColor: Color,
    textDarkColor: Color,
    textBlackColor: Color,
    hintLightColor: Color,
    hintDarkColor: Color,
    card: Color,
    background: Color,
    schoolRed: Color,
    schoolBlue: Color,
    info: Color,
    warn: Color,
    success: Color,
    error: Color,
) {
    var textLightColor: Color by mutableStateOf(textLightColor)
        internal set
    var textDarkColor: Color by mutableStateOf(textDarkColor)
        internal set
    var textBlackColor: Color by mutableStateOf(textBlackColor)
        internal set
    var hintLightColor: Color by mutableStateOf(hintLightColor)
        internal set
    var hintDarkColor: Color by mutableStateOf(hintDarkColor)
        internal set
    var card: Color by mutableStateOf(card)
        internal set
    var background: Color by mutableStateOf(background)
        private set
    var schoolRed: Color by mutableStateOf(schoolRed)
        private set
    var schoolBlue: Color by mutableStateOf(schoolBlue)
        private set
    var info: Color by mutableStateOf(info)
        private set
    var warn: Color by mutableStateOf(warn)
        private set
    var success: Color by mutableStateOf(success)
        private set
    var error: Color by mutableStateOf(error)
        private set
}

@Composable
fun AppTheme(
    theme: AppTheme.Theme = if (isSystemInDarkTheme()) AppTheme.Theme.Dark else AppTheme.Theme.Light,
    content: @Composable () -> Unit
) {

    val targetColors = when (theme) {
        AppTheme.Theme.Light -> LightColorPalette
        AppTheme.Theme.Dark -> DarkColorPalette
    }

    val textLightColor = animateColorAsState(targetColors.textLightColor, TweenSpec(600))
    val textDarkColor = animateColorAsState(targetColors.textDarkColor, TweenSpec(600))
    val textBlackColor = animateColorAsState(targetColors.textBlackColor, TweenSpec(600))
    val hintLightColor = animateColorAsState(targetColors.hintLightColor, TweenSpec(600))
    val hintDarkColor = animateColorAsState(targetColors.hintDarkColor, TweenSpec(600))
    val card = animateColorAsState(targetColors.card, TweenSpec(600))
    val background = animateColorAsState(targetColors.background, TweenSpec(600))
    val schoolRed = animateColorAsState(targetColors.schoolRed, TweenSpec(600))
    val schoolBlue = animateColorAsState(targetColors.schoolBlue, TweenSpec(600))
    val info = animateColorAsState(targetColors.info, TweenSpec(600))
    val warn = animateColorAsState(targetColors.warn, TweenSpec(600))
    val success = animateColorAsState(targetColors.success, TweenSpec(600))
    val error = animateColorAsState(targetColors.error, TweenSpec(600))
    val appColors = AppColors(
        textLightColor = textLightColor.value,
        textDarkColor = textDarkColor.value,
        textBlackColor = textBlackColor.value,
        hintLightColor = hintLightColor.value,
        hintDarkColor = hintDarkColor.value,
        card = card.value,
        background = background.value,
        schoolRed = schoolRed.value,
        schoolBlue = schoolBlue.value,
        info = info.value,
        warn = warn.value,
        success = success.value,
        error = error.value,
    )

    CompositionLocalProvider(LocalAppColors provides appColors, content = content)
}