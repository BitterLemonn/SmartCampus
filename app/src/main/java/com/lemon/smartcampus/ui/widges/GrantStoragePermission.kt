package com.lemon.smartcampus.ui.widges

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState

enum class PermissionType {
    READ
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GrantPermission(
    isShow: MutableState<Boolean>,
    permission: PermissionType,
    textDenied: String,
    textBlock: String,
    doAfterPermission: () -> Unit
) {
    val permissionGet = rememberPermissionState(
        permission = when (permission) {
            PermissionType.READ -> android.Manifest.permission.READ_EXTERNAL_STORAGE
        }
    )
    when (permissionGet.status) {
        is PermissionStatus.Denied -> {
            val textShow =
                if ((permissionGet.status as PermissionStatus.Denied).shouldShowRationale) textDenied
                else textBlock
            if (isShow.value)
            HintDialog(
                hint = textShow,
                onClickCancel = {
                    isShow.value = false
                },
                onClickCertain = {
                    permissionGet.launchPermissionRequest()
                },
                highLineCertain = true
            )
        }
        is PermissionStatus.Granted -> doAfterPermission.invoke()
    }
}