package com.lemon.smartcampus.ui.authPage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun HelpPage(
    navController: NavController?
) {
    Box(modifier = Modifier
        .padding(10.dp)
        .size(20.dp)
        .clickable(
            indication = rememberRipple(),
            interactionSource = MutableInteractionSource(),
            onClick = { navController?.popBackStack() }
        ))
}

@Composable
@Preview
private fun HelpPagePreview() {
    HelpPage(navController = null)
}