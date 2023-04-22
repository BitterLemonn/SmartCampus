package com.lemon.smartcampus.ui.widges

import androidx.compose.material.ScaffoldState
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarData
import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.Composable
import com.lemon.smartcampus.ui.theme.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

const val SNACK_INFO = "确定"
const val SNACK_WARN = " "
const val SNACK_ERROR = "  "
const val SNACK_SUCCESS = "OK"

@Composable
fun AppSnackBar(data: SnackbarData) {
    Snackbar(
        snackbarData = data,
        backgroundColor = when (data.actionLabel) {
            SNACK_INFO -> AppTheme.colors.info
            SNACK_WARN -> AppTheme.colors.warn
            SNACK_ERROR -> AppTheme.colors.error
            SNACK_SUCCESS -> AppTheme.colors.success
            else -> AppTheme.colors.background
        }
    )
}

fun popupSnackBar(
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    label: String,
    message: String,
    onDismissCallback: () -> Unit = {}
) {
    scope.launch {
        if (scaffoldState.drawerState.isOpen)
            scaffoldState.drawerState.close()
        scaffoldState.snackbarHostState.showSnackbar(
            actionLabel = label,
            message = message,
            duration = SnackbarDuration.Short
        )
        onDismissCallback.invoke()
    }
}