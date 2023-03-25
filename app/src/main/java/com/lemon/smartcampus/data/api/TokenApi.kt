package com.lemon.smartcampus.data.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.lemon.smartcampus.data.common.CommonInterceptor
import com.lemon.smartcampus.data.common.TokenUrl
import com.lemon.smartcampus.utils.ResponseData
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface TokenApi {
    @POST("send")
    @FormUrlEncoded
    suspend fun requestToken(
        @Field("phone") phone: String
    ): ResponseData<String>

    companion object {
        /**
         * 获取接口实例用于调用对接方法
         * @return ServerApi
         */
        @OptIn(ExperimentalSerializationApi::class)
        fun create(): TokenApi {
            val client = OkHttpClient.Builder()
                .addInterceptor(CommonInterceptor())
                .build()
            return Retrofit.Builder()
                .baseUrl(TokenUrl)
                .addConverterFactory(
                    Json.asConverterFactory(contentType = "application/json".toMediaType())
                )
                .client(client)
                .build()
                .create(TokenApi::class.java)
        }
    }
}