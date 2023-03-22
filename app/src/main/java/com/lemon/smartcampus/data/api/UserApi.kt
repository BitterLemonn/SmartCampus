package com.lemon.smartcampus.data.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.lemon.smartcampus.data.common.CommonInterceptor
import com.lemon.smartcampus.data.common.UserUrl
import com.lemon.smartcampus.data.database.entities.ProfileEntity
import com.lemon.smartcampus.data.database.networkEntities.CodeLoginEntity
import com.lemon.smartcampus.data.database.networkEntities.PasswordLoginEntity
import com.lemon.smartcampus.data.database.networkEntities.RegisterEntity
import com.lemon.smartcampus.utils.ResponseData
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.http.*

/**
 * 用于连接后台API的接口类，使用create函数获取实例调用对接方法
 */
interface UserApi {

    @POST("passwordLogin")
    suspend fun loginWithPassword(
        @Body body: PasswordLoginEntity
    ): ResponseData<ProfileEntity>

    @POST("codeLogin")
    suspend fun loginWithToken(
        @Body body: CodeLoginEntity
    ): ResponseData<ProfileEntity>

    @POST("register")
    suspend fun register(
        @Body body: RegisterEntity
    ): ResponseData<ProfileEntity>

    @POST("logOut")
    suspend fun logout(
        @Header("token") token: String
    ): ResponseData<String?>

    @POST("editAvatar")
    suspend fun changeAvatar(
        @Header("token") token: String,
        @Body body: RequestBody
    ): ResponseData<String>

    companion object {
        /**
         * 获取接口实例用于调用对接方法
         * @return ServerApi
         */
        @OptIn(ExperimentalSerializationApi::class)
        fun create(): UserApi {
            val client = OkHttpClient.Builder()
                .addInterceptor(CommonInterceptor())
                .build()
            return Retrofit.Builder()
                .baseUrl(UserUrl)
                .addConverterFactory(
                    Json.asConverterFactory(contentType = "application/json".toMediaType())
                )
                .client(client)
                .build()
                .create(UserApi::class.java)
        }
    }
}