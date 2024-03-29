package com.lemon.smartcampus.data.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.lemon.smartcampus.data.common.CommonInterceptor
import com.lemon.smartcampus.data.common.TopicContent
import com.lemon.smartcampus.data.common.TopicUrl
import com.lemon.smartcampus.data.database.entities.TopicEntity
import com.lemon.smartcampus.data.database.networkEntities.CommentResponseEntity
import com.lemon.smartcampus.data.database.networkEntities.TopicCommentEntity
import com.lemon.smartcampus.data.database.networkEntities.TopicPublishEntity
import com.lemon.smartcampus.data.database.networkEntities.TopicResponseEntity
import com.lemon.smartcampus.data.globalData.AppContext
import com.lemon.smartcampus.utils.JsonConverter
import com.lemon.smartcampus.utils.ResponseData
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.http.*

interface TopicApi {

    @POST("addTopic")
    suspend fun addTopic(
        @Header("token") token: String,
        @Body body: TopicPublishEntity
    ): ResponseData<String>

    @POST("addResource")
    suspend fun addResource(
        @Header("token") token: String,
        @Body body: RequestBody
    ): ResponseData<String>

    @GET("topicList/{page}/{count}")
    suspend fun getTopicPageList(
        @Path("page") curPage: Int,
        @Path("count") count: Int
    ): ResponseData<TopicResponseEntity>

    @GET("resourceList/{page}/{count}")
    suspend fun getResPageList(
        @Path("page") curPage: Int,
        @Path("count") count: Int
    ): ResponseData<TopicResponseEntity>

    @GET("topicCommentList/{page}/{count}/{topicId}")
    suspend fun getCommentList(
        @Path("page") curPage: Int,
        @Path("count") count: Int,
        @Path("topicId") id: String
    ): ResponseData<CommentResponseEntity>

    @POST("addComment")
    suspend fun sendComment(
        @Header("token") token: String,
        @Body body: TopicCommentEntity
    ): ResponseData<String>

    @DELETE("deleteTopic/{topicId}")
    suspend fun delPost(
        @Header("token") token: String,
        @Path("topicId") topicId: String
    ): ResponseData<String>

    @GET("userTopicList")
    suspend fun getUserTopic(
        @Header("token") token: String,
    ): ResponseData<List<TopicEntity>>

    @GET("userResourceList")
    suspend fun getUserResource(
        @Header("token") token: String,
    ): ResponseData<List<TopicEntity>>


    companion object {
        /**
         * 获取接口实例用于调用对接方法
         * @return TopicApi
         */
        @OptIn(ExperimentalSerializationApi::class)
        fun create(): TopicApi {
            val client = OkHttpClient.Builder()
                .addInterceptor(CommonInterceptor())
                .build()
            return Retrofit.Builder()
                .baseUrl(if (AppContext.publicUrl.isBlank()) TopicUrl else "${AppContext.publicUrl}$TopicContent")
                .addConverterFactory(
                    JsonConverter.Json.asConverterFactory(contentType = "application/json".toMediaType())
                )
                .client(client)
                .build()
                .create(TopicApi::class.java)
        }
    }
}