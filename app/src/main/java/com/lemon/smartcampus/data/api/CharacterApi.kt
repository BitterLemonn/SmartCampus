package com.lemon.smartcampus.data.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.lemon.smartcampus.data.common.CharacterUrl
import com.lemon.smartcampus.data.common.CommonInterceptor
import com.lemon.smartcampus.data.database.entities.CharacterEntity
import com.lemon.smartcampus.utils.JsonConverter
import com.lemon.smartcampus.utils.ResponseData
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.http.GET

interface CharacterApi {
    @GET("scholarList")
    suspend fun getScholarList(): ResponseData<List<CharacterEntity>>

    @GET("characterList")
    suspend fun getCharacterList(): ResponseData<List<CharacterEntity>>

    companion object {
        /**
         * 获取接口实例用于调用对接方法
         * @return ServerApi
         */
        @OptIn(ExperimentalSerializationApi::class)
        fun create(): CharacterApi {
            val client = OkHttpClient.Builder()
                .addInterceptor(CommonInterceptor())
                .build()
            return Retrofit.Builder()
                .baseUrl(CharacterUrl)
                .addConverterFactory(
                    JsonConverter.Json.asConverterFactory(contentType = "application/json".toMediaType())
                )
                .client(client)
                .build()
                .create(CharacterApi::class.java)
        }
    }
}