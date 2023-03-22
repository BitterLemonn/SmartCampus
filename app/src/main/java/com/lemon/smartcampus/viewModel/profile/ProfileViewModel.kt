package com.lemon.smartcampus.viewModel.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lemon.smartcampus.data.database.database.GlobalDataBase
import com.lemon.smartcampus.data.database.entities.TopicEntity
import com.lemon.smartcampus.data.globalData.AppContext
import com.lemon.smartcampus.data.repository.ProfileRepository
import com.lemon.smartcampus.utils.LoginException
import com.lemon.smartcampus.utils.NetworkState
import com.orhanobut.logger.Logger
import com.zj.mvi.core.SharedFlowEvents
import com.zj.mvi.core.setEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.File
import java.net.ConnectException
import java.net.SocketTimeoutException

class ProfileViewModel : ViewModel() {
    private val repository = ProfileRepository.getInstance()

    private val _viewStates = MutableStateFlow(ProfileViewState())
    val viewStates = _viewStates.asStateFlow()
    private val _viewEvents = SharedFlowEvents<ProfileViewEvent>()
    val viewEvents = _viewEvents.asSharedFlow()

    fun dispatch(viewAction: ProfileViewAction) {
        when (viewAction) {
            is ProfileViewAction.Logout -> logout()
            is ProfileViewAction.ChangeAvatar -> changeAvatar(viewAction.path)
            is ProfileViewAction.ChangeBackground -> changeBackground(viewAction.path)
        }
    }

    private fun logout() {
        viewModelScope.launch {
            flow {
                logoutLogic()
                emit("登出成功")
            }.onStart {
                // 避免无网络连接(直接本地先清理)
                AppContext.profile = null
                // 移除持久化
                GlobalDataBase.database.profileDao().deleteAll()
            }.onEach {
                _viewEvents.setEvent(ProfileViewEvent.Recompose)
            }.catch {
                // 非网络问题弹出提示
                if (it !is ConnectException && it !is SocketTimeoutException && it !is HttpException)
                    _viewEvents.setEvent(ProfileViewEvent.ShowToast(it.message!!))
            }.flowOn(Dispatchers.IO).collect()
        }
    }

    private suspend fun logoutLogic() {
        when (val result = repository.logout()) {
            is NetworkState.Error -> throw result.e ?: Exception(result.msg)
            else -> {}
        }
    }

    private fun changeAvatar(path: String) {
        viewModelScope.launch {
            flow {
                changeAvatarLogic(path)
                emit("更改成功")
            }.onStart {
                _viewEvents.setEvent(ProfileViewEvent.ShowLoadingDialog)
            }.onEach {
                _viewEvents.setEvent(ProfileViewEvent.Recompose)
            }.catch {
                _viewEvents.setEvent(ProfileViewEvent.ShowToast(it.message!!))
            }.onCompletion {
                _viewEvents.setEvent(ProfileViewEvent.DismissLoadingDialog)
            }.flowOn(Dispatchers.IO).collect()
        }
    }

    private suspend fun changeAvatarLogic(path: String) {
        val file = File(path)
        val id = AppContext.profile?.id
        Logger.d("file size: ${file.length()}")
        if (file.length() > 0) {
            id?.let {
                when (val result = repository.changAvatar(file, it)) {
                    is NetworkState.Success -> {
                        result.data?.let { avatar ->
                            AppContext.profile = AppContext.profile?.copy(avatar = avatar)
                        }
                    }
                    is NetworkState.Error -> throw result.e ?: Exception(result.msg)
                }
            } ?: throw LoginException("请先登录")
        } else throw Exception("获取文件失败")
    }

    private fun changeBackground(path: String) {

    }
}

data class ProfileViewState(
    val topicList: List<TopicEntity> = listOf(),
    val resList: List<TopicEntity> = listOf()
)

sealed class ProfileViewAction {
    object Logout : ProfileViewAction()
    data class ChangeAvatar(val path: String) : ProfileViewAction()
    data class ChangeBackground(val path: String) : ProfileViewAction()
}

sealed class ProfileViewEvent {
    object ShowLoadingDialog : ProfileViewEvent()
    object DismissLoadingDialog : ProfileViewEvent()
    object Recompose : ProfileViewEvent()
    data class ShowToast(val msg: String) : ProfileViewEvent()
}