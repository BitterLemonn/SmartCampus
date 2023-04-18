package com.lemon.smartcampus.data.api

import com.lemon.smartcampus.data.common.BaseUrl
import com.lemon.smartcampus.data.common.CommonInterceptor
import com.lemon.smartcampus.utils.ResponseData
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Url

interface DownloadApi {
    @GET
    suspend fun download(
        @Url url: String
    ): ResponseBody

    companion object {
        /**
         * 获取接口实例用于调用对接方法
         * @return ServerApi
         */
        fun create(): DownloadApi {
            val client = OkHttpClient.Builder()
                .addInterceptor(CommonInterceptor())
                .build()
            return Retrofit.Builder()
                .client(client)
                .baseUrl(BaseUrl)
                .build()
                .create(DownloadApi::class.java)
        }
    }
}