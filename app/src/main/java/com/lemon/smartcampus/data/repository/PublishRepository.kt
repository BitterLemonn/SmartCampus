package com.lemon.smartcampus.data.repository

import com.lemon.smartcampus.data.api.TopicApi
import com.lemon.smartcampus.data.database.networkEntities.TopicPublishEntity
import com.lemon.smartcampus.data.globalData.AppContext
import com.lemon.smartcampus.utils.NetworkState
import com.lemon.smartcampus.utils.UnifiedExceptionHandler
import com.orhanobut.logger.Logger
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class PublishRepository {
    companion object {
        @Volatile
        private var instance: PublishRepository? = null
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: PublishRepository().also { instance = it }
        }
    }

    suspend fun addTopic(
        userId: String,
        content: String,
        tags: List<String>
    ): NetworkState<String> {
        val body = TopicPublishEntity(userId, content, tags)
        val token = AppContext.profile?.token
        return UnifiedExceptionHandler.handleSuspendWithToken {
            TopicApi.create().addTopic(body = body, token = token!!)
        }
    }

    suspend fun addRes(
        userId: String,
        content: String,
        tags: List<String>,
        file: File
    ): NetworkState<String> {
        val avatarRequestBody = file.asRequestBody("*/*".toMediaTypeOrNull())
        val part = MultipartBody.Builder().addFormDataPart("userId", userId)
            .addFormDataPart("topicContent", content)
            .addFormDataPart("topicTags", tags.joinToString { it })
            .addFormDataPart("multipartFile", file.name, avatarRequestBody)
            .setType(MultipartBody.FORM)
            .build()
        val token = AppContext.profile?.token
        return UnifiedExceptionHandler.handleSuspendWithToken {
            TopicApi.create().addResource(body = part, token = token!!)
        }
    }
}