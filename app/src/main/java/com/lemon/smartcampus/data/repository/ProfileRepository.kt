package com.lemon.smartcampus.data.repository

import com.lemon.smartcampus.data.api.UserApi
import com.lemon.smartcampus.data.globalData.AppContext
import com.lemon.smartcampus.utils.NetworkState
import com.lemon.smartcampus.utils.UnifiedExceptionHandler
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ProfileRepository {
    companion object {
        @Volatile
        private var instance: ProfileRepository? = null
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: ProfileRepository().also { instance = it }
        }
    }

    suspend fun logout(): NetworkState<String?> {
        return AppContext.profile?.token?.let {
            UnifiedExceptionHandler.handleSuspend { UserApi.create().logout(it) }
        } ?: NetworkState.Error("获取token失败")
    }

    suspend fun changAvatar(file: File, id: String): NetworkState<String> {
        val avatarRequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val part = MultipartBody.Builder().addFormDataPart("userId", id)
            .addFormDataPart("multipartFile", file.name, avatarRequestBody)
            .setType(MultipartBody.FORM)
            .build()
        return AppContext.profile?.token?.let {
            UnifiedExceptionHandler.handleSuspend {
                UserApi.create().changeAvatar(token = it, body = part)
            }
        } ?: NetworkState.Error("获取token失败")
    }
}