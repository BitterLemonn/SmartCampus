package com.lemon.smartcampus.viewModel.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lemon.smartcampus.data.database.database.GlobalDataBase
import com.lemon.smartcampus.data.database.entities.AcademicEntity
import com.lemon.smartcampus.data.database.entities.NewsEntity
import com.lemon.smartcampus.data.globalData.AppContext
import com.lemon.smartcampus.data.repository.InfoRepository
import com.lemon.smartcampus.utils.INFO_REFRESH_TIME
import com.lemon.smartcampus.utils.NetworkState
import com.zj.mvi.core.SharedFlowEvents
import com.zj.mvi.core.setEvent
import com.zj.mvi.core.setState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class InfoViewModel : ViewModel() {
    private val repository = InfoRepository.getInstance()

    private val _viewStates = MutableStateFlow(InfoViewStates())
    val viewStates = _viewStates.asStateFlow()
    private val _viewEvents = SharedFlowEvents<InfoViewEvent>()
    val viewEvents = _viewEvents.asSharedFlow()

    fun dispatch(viewAction: InfoViewAction) {
        when (viewAction) {
            is InfoViewAction.Request -> request()
        }
    }

    private fun request() {
        val refreshTime = AppContext.profile?.refreshTime?.getOrDefault(INFO_REFRESH_TIME, 0L) ?: 0L
        var requestFlag = false
        viewModelScope.launch(Dispatchers.IO) {
            val newsList = GlobalDataBase.database.infoDao().getHomePageNews() ?: listOf()
            val academicList =
                GlobalDataBase.database.infoDao().getHomePageAcademic() ?: listOf()
            // 或数据库中未找到信息时请求服务器
            if (newsList.isEmpty() || academicList.isEmpty()) requestFlag = true
            else _viewStates.setState { copy(newsList = newsList, academicList = academicList) }
        }
        // 30分钟请求一次服务器
        if (System.currentTimeMillis() - refreshTime >= 30 * 60 * 1_000) requestFlag = true
        if (requestFlag) {
            viewModelScope.launch {
                flow {
                    requestLogic()
                    emit("请求成功")
                }.onEach {
                    AppContext.profile?.refreshTime?.let {
                        it[INFO_REFRESH_TIME] = System.currentTimeMillis()
                    }
                    // 更新profile持久化
                    GlobalDataBase.database.profileDao().deleteAll()
                    GlobalDataBase.database.profileDao().insert(AppContext.profile!!)
                }.catch {
                    _viewEvents.setEvent(InfoViewEvent.ShowToast(it.message!!))
                }.flowOn(Dispatchers.IO).collect()
            }
        }
    }

    private suspend fun requestLogic() {
        val newsList = mutableListOf<NewsEntity>()
        val academicList = mutableListOf<AcademicEntity>()
        when (val result = repository.getHomepageNewsList()) {
            is NetworkState.Success -> newsList.addAll(result.data!!.list)
            is NetworkState.Error -> throw Exception(result.msg)
        }
        when (val result = repository.getHomepageAcademicList()) {
            is NetworkState.Success -> academicList.addAll(result.data!!.list)
            is NetworkState.Error -> throw Exception(result.msg)
        }
        // 持久化
        GlobalDataBase.database.infoDao().insertNewsList(newsList)
        GlobalDataBase.database.infoDao().insertAcademicList(academicList)
        _viewStates.setState { copy(newsList = newsList, academicList = academicList) }
    }
}

data class InfoViewStates(
    val newsList: List<NewsEntity> = listOf(),
    val academicList: List<AcademicEntity> = listOf()
)

sealed class InfoViewAction {
    object Request : InfoViewAction()
}

sealed class InfoViewEvent {
    data class ShowToast(val msg: String) : InfoViewEvent()
}