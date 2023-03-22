package com.lemon.smartcampus.data.repository

import com.lemon.smartcampus.data.api.TokenApi
import com.lemon.smartcampus.data.api.UserApi
import com.lemon.smartcampus.data.database.entities.ProfileEntity
import com.lemon.smartcampus.data.database.networkEntities.CodeLoginEntity
import com.lemon.smartcampus.data.database.networkEntities.PasswordLoginEntity
import com.lemon.smartcampus.data.database.networkEntities.RegisterEntity
import com.lemon.smartcampus.utils.JsonConverter
import com.lemon.smartcampus.utils.NetworkState
import com.lemon.smartcampus.utils.NoNeedResponse
import com.lemon.smartcampus.utils.UnifiedExceptionHandler
import kotlinx.serialization.encodeToString
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AuthRepository {
    companion object {
        @Volatile
        private var instance: AuthRepository? = null
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: AuthRepository().also { instance = it }
        }
    }

    suspend fun loginWithPassword(phone: String, password: String): NetworkState<ProfileEntity> {
        val body = PasswordLoginEntity(phone, password)
        return UnifiedExceptionHandler.handleSuspend {
            UserApi.create().loginWithPassword(body)
        }
    }

    suspend fun loginWithToken(phone: String, token: String): NetworkState<ProfileEntity> {
        val body = CodeLoginEntity(phone, token)
        return UnifiedExceptionHandler.handleSuspend {
            UserApi.create().loginWithToken(body)
        }
    }

    suspend fun requestToken(phone: String): NetworkState<String> {
        return UnifiedExceptionHandler.handleSuspend {
            TokenApi.create().requestToken(phone)
        }
    }

    suspend fun register(
        phone: String,
        password: String,
        token: String
    ): NetworkState<ProfileEntity> {
        val body = RegisterEntity(phone, password, token)
        return UnifiedExceptionHandler.handleSuspend {
            UserApi.create().register(body)
        }
    }
}