package com.lemon.smartcampus.data.repository

import com.lemon.smartcampus.data.api.UserApi
import com.lemon.smartcampus.data.database.networkEntities.ChangeNicknameEntity
import com.lemon.smartcampus.data.database.networkEntities.ChangeTagsEntity
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

    suspend fun logout(token: String): NetworkState<String?> {
        return UnifiedExceptionHandler.handleSuspend { UserApi.create().logout(token = token) }
    }

    suspend fun changeAvatar(file: File, id: String): NetworkState<String> {
        val avatarRequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val part = MultipartBody.Builder().addFormDataPart("userId", id)
            .addFormDataPart("multipartFile", file.name, avatarRequestBody)
            .setType(MultipartBody.FORM)
            .build()
        val token = AppContext.profile?.token
        return UnifiedExceptionHandler.handleSuspendWithToken {
            UserApi.create().changeAvatar(token = token!!, body = part)
        }
    }

    suspend fun changBackground(file: File, id: String): NetworkState<String> {
        val avatarRequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val part = MultipartBody.Builder().addFormDataPart("userId", id)
            .addFormDataPart("multipartFile", file.name, avatarRequestBody)
            .setType(MultipartBody.FORM)
            .build()
        val token = AppContext.profile?.token
        return UnifiedExceptionHandler.handleSuspendWithToken {
            UserApi.create().changeBackground(token = token!!, body = part)
        }
    }

    suspend fun changeNickname(id: String, nickname: String): NetworkState<String?> {
        val body = ChangeNicknameEntity(userId = id, nickname = nickname)
        val token = AppContext.profile?.token
        return UnifiedExceptionHandler.handleSuspendWithToken {
            UserApi.create().changeNickname(token = token!!, body = body)
        }
    }

    suspend fun changeTags(id: String, tags: List<String>): NetworkState<String?> {
        val body = ChangeTagsEntity(userId = id, tags = tags)
        val token = AppContext.profile?.token
        return UnifiedExceptionHandler.handleSuspendWithToken {
            UserApi.create().changeTags(token = token!!, body = body)
        }
    }
}
