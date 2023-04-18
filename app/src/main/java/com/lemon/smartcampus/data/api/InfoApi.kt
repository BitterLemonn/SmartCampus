package com.lemon.smartcampus.data.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.lemon.smartcampus.data.common.CommonInterceptor
import com.lemon.smartcampus.data.common.InfoUrl
import com.lemon.smartcampus.data.database.entities.AcademicEntity
import com.lemon.smartcampus.data.database.entities.InfoEntity
import com.lemon.smartcampus.data.database.entities.NewsEntity
import com.lemon.smartcampus.utils.JsonConverter
import com.lemon.smartcampus.utils.ResponseData
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path

interface InfoApi {
    @GET("newsList/1/4")
    suspend fun getHomePageNewsList(): ResponseData<InfoEntity<NewsEntity>>

    @GET("informationList/1/4")
    suspend fun getHomePageAcademicList(): ResponseData<InfoEntity<AcademicEntity>>

    @GET("newsList/{page}/{count}")
    suspend fun getNewsList(
        @Path("page") page: Int,
        @Path("count") count: Int
    ): ResponseData<InfoEntity<NewsEntity>>

    @GET("informationList/{page}/{count}")
    suspend fun getAcademicList(
        @Path("page") page: Int,
        @Path("count") count: Int
    ): ResponseData<InfoEntity<AcademicEntity>>

    companion object {
        /**
         * 获取接口实例用于调用对接方法
         * @return ServerApi
         */
        @OptIn(ExperimentalSerializationApi::class)
        fun create(): InfoApi {
            val client = OkHttpClient.Builder()
                .addInterceptor(CommonInterceptor())
                .build()
            return Retrofit.Builder()
                .baseUrl(InfoUrl)
                .addConverterFactory(
                    JsonConverter.Json.asConverterFactory(contentType = "application/json".toMediaType())
                )
                .client(client)
                .build()
                .create(InfoApi::class.java)
        }
    }
}