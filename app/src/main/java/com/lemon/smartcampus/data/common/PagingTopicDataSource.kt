package com.lemon.smartcampus.data.common

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lemon.smartcampus.data.database.entities.TopicEntity
import com.lemon.smartcampus.data.repository.TopicRepository
import com.lemon.smartcampus.utils.NetworkState
import com.orhanobut.logger.Logger


class PagingTopicDataSource(private val topicMode: Boolean) : PagingSource<Int, TopicEntity>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TopicEntity> {
        val currentPage = params.key ?: 1

        Logger.d("请求${if (topicMode) "话题" else "资源"}列表数据, 页码:$currentPage")

        return when (
            val result = if (topicMode)
                TopicRepository.getInstance().getTopicPageList(currentPage)
            else TopicRepository.getInstance().getResPageList(currentPage)
        ) {
            is NetworkState.Success -> {
                val nextPage = if (currentPage < (result.data?.totalPage ?: 0)) {
                    currentPage + 1
                } else null
                LoadResult.Page(
                    data = result.data!!.list,
                    prevKey = null,
                    nextKey = nextPage
                )
            }
            is NetworkState.Error -> LoadResult.Error(throwable = result.e ?: Exception(result.msg))
        }
    }

    override fun getRefreshKey(state: PagingState<Int, TopicEntity>): Int? {
        return null
    }
}