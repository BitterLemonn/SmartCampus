package com.lemon.smartcampus.viewModel.topic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lemon.smartcampus.data.database.entities.CommentEntity
import com.lemon.smartcampus.data.repository.TopicRepository
import com.lemon.smartcampus.utils.NetworkState
import com.orhanobut.logger.Logger
import com.zj.mvi.core.SharedFlowEvents
import com.zj.mvi.core.setEvent
import com.zj.mvi.core.setState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {
    private val repository = TopicRepository.getInstance()

    private val _viewStates = MutableStateFlow(DetailViewState())
    val viewStates = _viewStates.asStateFlow()
    private val _viewEvents = SharedFlowEvents<DetailViewEvent>()
    val viewEvents = _viewEvents.asSharedFlow()

    fun dispatch(viewAction: DetailViewAction) {
        when (viewAction) {
            is DetailViewAction.SendChat -> sendChat()
            is DetailViewAction.UpdateAtTop -> _viewStates.setState { copy(atTop = viewAction.atTop) }
            is DetailViewAction.UpdateTopicId -> _viewStates.setState { copy(topicId = viewAction.topicId) }
            is DetailViewAction.UpdateChat -> _viewStates.setState { copy(chat = viewAction.chat) }
            is DetailViewAction.GetNextPage -> getPage(false)
            is DetailViewAction.Refresh -> getPage(true)
        }
    }

    private fun sendChat() {
        viewModelScope.launch {
            flow {
                sendChatLogic()
                emit("发表成功")
            }.onStart {
                _viewEvents.setEvent(DetailViewEvent.ShowLoadingDialog)
            }.onEach {
                _viewEvents.setEvent(DetailViewEvent.ShowToast("发表成功", true))
            }.catch {
                _viewEvents.setEvent(DetailViewEvent.ShowToast(it.message!!, false))
            }.onCompletion {
                _viewEvents.setEvent(DetailViewEvent.DismissLoadingDialog)
            }.flowOn(Dispatchers.IO).collect()
        }
    }

    private suspend fun sendChatLogic() {
        val chat = viewStates.value.chat
        val topicId = viewStates.value.topicId
        when (val result = repository.sendChat(chat = chat, topicId = topicId)) {
            is NetworkState.Success -> {
                _viewStates.setState { copy(chat = "") }
                getPage(true)
            }
            is NetworkState.Error -> throw result.e ?: Exception(result.msg)
        }
    }

    private fun getPage(isRefresh: Boolean) {
        viewModelScope.launch {
            flow {
                getPageLogic(isRefresh)
                emit("获取成功")
                Logger.d("获取页面成功: ${viewStates.value.commentList}")
            }.onStart {
                if (isRefresh) _viewEvents.setEvent(DetailViewEvent.ShowLoadingDialog)
            }.catch {
                if (it is AllLoadException) {
                    if (!viewStates.value.atTop) {
                        _viewEvents.setEvent(
                            DetailViewEvent.ShowToast("已加载全部评论", true)
                        )
                        _viewStates.setState { copy(throwToast = true) }
                    }
                } else _viewEvents.setEvent(
                    DetailViewEvent.ShowToast(it.message!!, false)
                )
            }.onCompletion { _viewEvents.setEvent(DetailViewEvent.DismissLoadingDialog) }
                .flowOn(Dispatchers.IO).collect()
        }
    }

    private suspend fun getPageLogic(isRefresh: Boolean) {
        if (isRefresh) _viewStates.setState { copy(loadAll = false, throwToast = false) }
        if (viewStates.value.loadAll && !viewStates.value.throwToast) throw AllLoadException("已加载全部页面")

        val loadPage = if (isRefresh) 1 else viewStates.value.curPage + 1
        val topicId = viewStates.value.topicId
        if (topicId == "-1") throw Exception("页面被外星人带走了,请返回重试")
        when (val result = repository.getCommentList(loadPage, topicId)) {
            is NetworkState.Error -> throw result.e ?: Exception(result.msg)
            is NetworkState.Success -> {
                val data = result.data!!
                val commentList = (if (isRefresh) listOf() else
                    viewStates.value.commentList) + data.list
                _viewStates.setState {
                    copy(
                        curPage = data.currPage,
                        loadAll = data.currPage == data.totalPage,
                        commentList = commentList
                    )
                }
                Logger.d("state: ${viewStates.value.commentList}")
            }
        }
    }

    private suspend fun delPostLogic(){
        val topicId = viewStates.value.topicId

    }

}

data class DetailViewState(
    val topicId: String = "-1",
    val chat: String = "",
    val commentList: List<CommentEntity> = listOf(),
    val curPage: Int = 1,
    val atTop: Boolean = false,
    val loadAll: Boolean = false,
    val throwToast: Boolean = false
)

sealed class DetailViewAction {
    data class UpdateChat(val chat: String) : DetailViewAction()
    data class UpdateTopicId(val topicId: String) : DetailViewAction()
    data class UpdateAtTop(val atTop: Boolean) : DetailViewAction()
    object GetNextPage : DetailViewAction()
    object Refresh : DetailViewAction()
    object SendChat : DetailViewAction()
}

sealed class DetailViewEvent {
    object ShowLoadingDialog : DetailViewEvent()
    object DismissLoadingDialog : DetailViewEvent()
    data class ShowToast(val msg: String, val success: Boolean) : DetailViewEvent()
}

class AllLoadException(msg: String) : Exception(msg)