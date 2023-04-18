package com.lemon.smartcampus.data.repository

import com.lemon.smartcampus.data.api.InfoApi
import com.lemon.smartcampus.data.database.entities.AcademicEntity
import com.lemon.smartcampus.data.database.entities.InfoEntity
import com.lemon.smartcampus.data.database.entities.NewsEntity
import com.lemon.smartcampus.utils.COUNT_PER_PAGE
import com.lemon.smartcampus.utils.NetworkState
import com.lemon.smartcampus.utils.UnifiedExceptionHandler

class InfoRepository {
    companion object {
        @Volatile
        private var instance: InfoRepository? = null
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: InfoRepository().also { instance = it }
        }
    }

    suspend fun getHomepageNewsList(): NetworkState<InfoEntity<NewsEntity>> {
        return UnifiedExceptionHandler.handleSuspend { InfoApi.create().getHomePageNewsList() }
    }

    suspend fun getHomepageAcademicList(): NetworkState<InfoEntity<AcademicEntity>> {
        return UnifiedExceptionHandler.handleSuspend { InfoApi.create().getHomePageAcademicList() }
    }

    suspend fun getNewsList(page: Int, count: Int): NetworkState<InfoEntity<NewsEntity>> {
        return UnifiedExceptionHandler.handleSuspend {
            InfoApi.create().getNewsList(page = page, count = count)
        }
    }

    suspend fun getAcademicList(page: Int, count: Int): NetworkState<InfoEntity<AcademicEntity>> {
        return UnifiedExceptionHandler.handleSuspend {
            InfoApi.create().getAcademicList(page = page, count = count)
        }
    }
}