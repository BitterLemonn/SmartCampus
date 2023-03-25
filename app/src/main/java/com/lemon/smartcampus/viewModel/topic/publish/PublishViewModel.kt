package com.lemon.smartcampus.viewModel.topic.publish

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lemon.smartcampus.data.globalData.AppContext
import com.lemon.smartcampus.data.repository.PublishRepository
import com.lemon.smartcampus.utils.NetworkState
import com.lemon.smartcampus.utils.logoutLocal
import com.zj.mvi.core.SharedFlowEvents
import com.zj.mvi.core.setEvent
import com.zj.mvi.core.setState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import javax.security.auth.login.LoginException

class PublishViewModel : ViewModel() {
    private val repository = PublishRepository.getInstance()

    private val _viewStates = MutableStateFlow(PublishViewState())
    val viewStates = _viewStates.asStateFlow()
    private val _viewEvents = SharedFlowEvents<PublishViewEvent>()
    val viewEvents = _viewEvents.asSharedFlow()

    fun dispatch(viewAction: PublishViewAction) {
        when (viewAction) {
            is PublishViewAction.UpdateContent ->
                _viewStates.setState { copy(content = viewAction.content) }
            is PublishViewAction.UpdateTags ->
                _viewStates.setState { copy(tags = viewAction.tags) }
            is PublishViewAction.Publish -> publish(viewAction.topicMode)
            is PublishViewAction.UpdatePath ->
                _viewStates.setState { copy(path = viewAction.path) }
        }
    }

    private fun publish(topicMode: Boolean) {
        viewModelScope.launch {
            flow {
                if (topicMode) publishTopicLogic()
                else publishResLogic()
                emit("发表成功")
            }.onStart {
                _viewEvents.setEvent(PublishViewEvent.ShowLoadingDialog)
            }.onEach {
                _viewEvents.setEvent(PublishViewEvent.TransIntent)
            }.catch {
                if (it is LoginException) {
                    logoutLocal(viewModelScope)
                    _viewEvents.setEvent(PublishViewEvent.Logout)
                }
                _viewEvents.setEvent(PublishViewEvent.ShowToast(it.message!!, false))
            }.onCompletion {
                _viewEvents.setEvent(PublishViewEvent.DismissLoadingDialog)
            }.flowOn(Dispatchers.IO).collect()
        }
    }

    private suspend fun publishTopicLogic() {
        val content = viewStates.value.content
        val tags = viewStates.value.tags
        val id = AppContext.profile?.id!!
        if (content.isBlank())
            throw Exception("内容不能为空")
        when (val result = repository.addTopic(id, content, tags)) {
            is NetworkState.Error -> throw result.e ?: Exception(result.msg)
            is NetworkState.Success ->
                _viewEvents.setEvent(PublishViewEvent.ShowToast("发布成功", true))
        }
    }

    private suspend fun publishResLogic() {
        val content = viewStates.value.content
        val file = File(viewStates.value.path)
        val tags = viewStates.value.tags
        val id = AppContext.profile?.id!!
        if (!file.isFile) throw Exception("请先上传文件")
        if (content.isBlank()) throw Exception("内容不能为空")
        when (val result = repository.addRes(id, content, tags, file)) {
            is NetworkState.Error -> throw result.e ?: Exception(result.msg)
            is NetworkState.Success ->
                _viewEvents.setEvent(PublishViewEvent.ShowToast("发布成功", true))
        }
    }
}

data class PublishViewState(
    val content: String = "",
    val tags: List<String> = listOf(),
    val path: String = ""
)

sealed class PublishViewAction {
    data class UpdateContent(val content: String) : PublishViewAction()
    data class UpdateTags(val tags: List<String>) : PublishViewAction()
    data class UpdatePath(val path: String) : PublishViewAction()
    data class Publish(val topicMode: Boolean) : PublishViewAction()
}

sealed class PublishViewEvent {
    object ShowLoadingDialog : PublishViewEvent()
    object DismissLoadingDialog : PublishViewEvent()
    object TransIntent : PublishViewEvent()
    data class ShowToast(val msg: String, val success: Boolean) : PublishViewEvent()
    object Logout : PublishViewEvent()
}