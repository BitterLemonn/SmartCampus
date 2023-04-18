package com.lemon.smartcampus.viewModel.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lemon.smartcampus.data.database.entities.AcademicEntity
import com.lemon.smartcampus.data.database.entities.NewsEntity
import com.lemon.smartcampus.data.repository.InfoRepository
import com.lemon.smartcampus.ui.infoPage.InfoType
import com.lemon.smartcampus.utils.COUNT_PER_PAGE
import com.lemon.smartcampus.utils.NetworkState
import com.lemon.smartcampus.viewModel.topic.DetailViewEvent
import com.orhanobut.logger.Logger
import com.zj.mvi.core.SharedFlowEvents
import com.zj.mvi.core.setEvent
import com.zj.mvi.core.setState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class InfoListViewModel : ViewModel() {
    private val repository = InfoRepository.getInstance()

    private val _viewStates = MutableStateFlow(InfoListViewState())
    val viewStates = _viewStates.asStateFlow()
    private val _viewEvents = SharedFlowEvents<InfoListViewEvent>()
    val viewEvents = _viewEvents.asSharedFlow()

    fun dispatch(viewAction: InfoListViewAction) {
        when (viewAction) {
            is InfoListViewAction.GetNextPage -> requestPage(false)
            is InfoListViewAction.Refresh -> requestPage(true)
            is InfoListViewAction.UpdateType -> _viewStates.setState { copy(type = viewAction.type) }
        }
    }

    private fun requestPage(isRefresh: Boolean) {
        viewModelScope.launch {
            flow {
                requestPageLogic(isRefresh)
                emit("获取成功")
            }.onStart {
                if (isRefresh) _viewEvents.setEvent(InfoListViewEvent.ShowLoadingDialog)
            }.catch {
                DetailViewEvent.ShowToast(it.message!!, false)
            }.onCompletion { _viewEvents.setEvent(InfoListViewEvent.DismissLoadingDialog) }
                .flowOn(Dispatchers.IO).collect()
        }
    }

    private suspend fun requestPageLogic(isRefresh: Boolean) {
        if (isRefresh) _viewStates.setState { copy(loadAll = false) }

        val loadPage = if (isRefresh) 1 else viewStates.value.curPage + 1
        val type = viewStates.value.type

        if (viewStates.value.loading || viewStates.value.loadAll) return
        _viewStates.setState { copy(loading = true) }
        if (type == InfoType.NEWS) when (val result = repository.getNewsList(
            loadPage, COUNT_PER_PAGE * 2
        )) {
            is NetworkState.Error -> throw result.e ?: Exception(result.msg)
            is NetworkState.Success -> {
                val data = result.data!!
                Logger.d("data: ${data.list.map { it.id }}")
                val list = (if (isRefresh) listOf() else viewStates.value.newsList) + data.list
                _viewStates.setState {
                    copy(
                        curPage = data.currPage,
                        loading = false,
                        loadAll = data.currPage == data.totalPage,
                        newsList = list
                    )
                }
            }  
        } else when (val result = repository.getAcademicList(
            loadPage, COUNT_PER_PAGE * 4
        )) {
            is NetworkState.Error -> throw result.e ?: Exception(result.msg)
            is NetworkState.Success -> {
                val data = result.data!!
                val list =
                    (if (isRefresh) listOf() else viewStates.value.academicList) + data.list
                _viewStates.setState {
                    copy(
                        curPage = data.currPage,
                        loading = false,
                        loadAll = data.currPage == data.totalPage,
                        academicList = list
                    )
                }
            }
        }
    }
}

data class InfoListViewState(
    val academicList: List<AcademicEntity> = listOf(),
    val newsList: List<NewsEntity> = listOf(),
    val curPage: Int = 1,
    val loading: Boolean = false,
    val loadAll: Boolean = false,
    val type: Int = InfoType.NEWS
)

sealed class InfoListViewAction {
    object GetNextPage : InfoListViewAction()
    object Refresh : InfoListViewAction()
    data class UpdateType(val type: Int) : InfoListViewAction()
}

sealed class InfoListViewEvent {
    object ShowLoadingDialog : InfoListViewEvent()
    object DismissLoadingDialog : InfoListViewEvent()
    data class ShowToast(val msg: String, val success: Boolean) : InfoListViewEvent()
}