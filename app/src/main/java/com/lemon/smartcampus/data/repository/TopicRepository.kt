package com.lemon.smartcampus.data.repository

import com.lemon.smartcampus.data.api.DownloadApi
import com.lemon.smartcampus.data.api.TopicApi
import com.lemon.smartcampus.data.database.networkEntities.CommentResponseEntity
import com.lemon.smartcampus.data.database.networkEntities.TopicCommentEntity
import com.lemon.smartcampus.data.database.networkEntities.TopicResponseEntity
import com.lemon.smartcampus.data.globalData.AppContext
import com.lemon.smartcampus.utils.COUNT_PER_PAGE
import com.lemon.smartcampus.utils.NetworkState
import com.lemon.smartcampus.utils.UnifiedExceptionHandler
import okhttp3.ResponseBody

class TopicRepository {
    companion object {
        @Volatile
        private var instance: TopicRepository? = null
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: TopicRepository().also { instance = it }
        }
    }

    suspend fun getTopicPageList(curPage: Int): NetworkState<TopicResponseEntity> {
        return UnifiedExceptionHandler.handleSuspend {
            TopicApi.create().getTopicPageList(curPage = curPage, count = COUNT_PER_PAGE)
        }
    }

    suspend fun getResPageList(curPage: Int): NetworkState<TopicResponseEntity> {
        return UnifiedExceptionHandler.handleSuspend {
            TopicApi.create().getResPageList(curPage = curPage, count = COUNT_PER_PAGE)
        }
    }

    suspend fun getCommentList(curPage: Int, topicId: String): NetworkState<CommentResponseEntity> {
        return UnifiedExceptionHandler.handleSuspend {
            TopicApi.create()
                .getCommentList(curPage = curPage, count = COUNT_PER_PAGE, id = topicId)
        }
    }

    suspend fun sendChat(chat: String, topicId: String): NetworkState<String> {
        val userId = AppContext.profile!!.id
        val token = AppContext.profile?.token
        val body = TopicCommentEntity(
            userId = userId,
            topicId = topicId,
            comment = chat
        )
        return UnifiedExceptionHandler.handleSuspendWithToken {
            TopicApi.create().sendComment(token = token!!, body = body)
        }
    }

    suspend fun delPost(topicId: String): NetworkState<String> {
        val token = AppContext.profile?.token
        return UnifiedExceptionHandler.handleSuspendWithToken {
            TopicApi.create().delPost(token = token!!, topicId = topicId)
        }
    }

    suspend fun download(url: String): ResponseBody {
        return DownloadApi.create().download(url)
    }
}