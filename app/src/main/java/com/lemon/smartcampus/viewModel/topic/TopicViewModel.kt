package com.lemon.smartcampus.viewModel.topic

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.lemon.smartcampus.data.common.PagingTopicDataSource
import com.lemon.smartcampus.utils.COUNT_PER_PAGE

class TopicViewModel : ViewModel() {
    fun getPage(topicMode: Boolean) = Pager(
        PagingConfig(
            pageSize = COUNT_PER_PAGE,
            initialLoadSize = COUNT_PER_PAGE
        )
    ) {
        PagingTopicDataSource(topicMode)
    }.flow
}