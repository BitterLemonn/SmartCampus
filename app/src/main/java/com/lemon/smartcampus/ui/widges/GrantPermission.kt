package com.lemon.smartcampus.ui.widges

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState

enum class PermissionType {
    READ, CAMARA, WRITE, NOTIFICATION
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
            PermissionType.CAMARA -> android.Manifest.permission.CAMERA
            PermissionType.WRITE -> android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            PermissionType.NOTIFICATION -> if (Build.VERSION.SDK_INT > 32) android.Manifest.permission.POST_NOTIFICATIONS
            else android.Manifest.permission.ACCESS_NOTIFICATION_POLICY
        }
    )
    val items = listOf(
        BottomButtonItem("取消") { isShow.value = false },
        BottomButtonItem("确认") { permissionGet.launchPermissionRequest() }
    )
    when (permissionGet.status) {
        is PermissionStatus.Denied -> {
            val textShow =
                if ((permissionGet.status as PermissionStatus.Denied).shouldShowRationale) textDenied
                else textBlock
            BottomHintDialog(
                hint = textShow,
                items = items,
                isShow = isShow
            )
        }
        is PermissionStatus.Granted -> if (isShow.value) doAfterPermission.invoke()
    }
}