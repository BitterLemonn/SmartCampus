package com.lemon.smartcampus.viewModel.topic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.lemon.smartcampus.data.common.PagingTopicDataSource
import com.lemon.smartcampus.data.database.database.GlobalDataBase
import com.lemon.smartcampus.data.database.entities.TopicEntity
import com.lemon.smartcampus.data.globalData.AppContext
import com.lemon.smartcampus.utils.COUNT_PER_PAGE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TopicViewModel : ViewModel() {
    fun dispatch(viewAction: TopicViewAction) {
        when (viewAction) {
            is TopicViewAction.CachedList -> cacheList(viewAction.list)
        }
    }

    fun getPage(topicMode: Boolean) = Pager(
        PagingConfig(pageSize = COUNT_PER_PAGE, initialLoadSize = COUNT_PER_PAGE)
    ) {
        PagingTopicDataSource(topicMode)
    }.flow

    private fun cacheList(list: List<TopicEntity>) {
        if (list.isNotEmpty()) {
            list.forEach { it.isCached = true }
            viewModelScope.launch(Dispatchers.IO) {
                // 持久化缓存
                GlobalDataBase.database.cachedPostDao().insertAll(list)
            }
        }
    }
}

sealed class TopicViewAction {
    data class CachedList(val list: List<TopicEntity>) : TopicViewAction()
}