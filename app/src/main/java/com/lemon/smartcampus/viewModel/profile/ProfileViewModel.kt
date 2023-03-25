package com.lemon.smartcampus.viewModel.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lemon.smartcampus.data.database.database.GlobalDataBase
import com.lemon.smartcampus.data.database.entities.TopicEntity
import com.lemon.smartcampus.data.globalData.AppContext
import com.lemon.smartcampus.data.repository.ProfileRepository
import com.lemon.smartcampus.data.repository.TopicRepository
import com.lemon.smartcampus.utils.NetworkState
import com.lemon.smartcampus.utils.logoutLocal
import com.orhanobut.logger.Logger
import com.zj.mvi.core.SharedFlowEvents
import com.zj.mvi.core.setEvent
import com.zj.mvi.core.setState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.File
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.security.auth.login.LoginException

class ProfileViewModel : ViewModel() {
    private val repository = ProfileRepository.getInstance()

    private val _viewStates = MutableStateFlow(ProfileViewState())
    val viewStates = _viewStates.asStateFlow()
    private val _viewEvents = SharedFlowEvents<ProfileViewEvent>()
    val viewEvents = _viewEvents.asSharedFlow()

    fun dispatch(viewAction: ProfileViewAction) {
        when (viewAction) {
            is ProfileViewAction.Logout -> logout()
            is ProfileViewAction.ChangeAvatar -> changeUserImg(viewAction.path, true)
            is ProfileViewAction.ChangeBackground -> changeUserImg(viewAction.path, false)
            is ProfileViewAction.ChangeNickname -> changeNickname(viewAction.name)
            is ProfileViewAction.ChangeTags -> changeTags(viewAction.tags)
            is ProfileViewAction.GetUserPost -> getUserPost()
            is ProfileViewAction.DelPost -> delPost(topicId = viewAction.topicId)
        }
    }

    private fun logout() {
        viewModelScope.launch {
            flow {
                logoutLogic()
                emit("登出成功")
            }.onEach {
                _viewEvents.setEvent(ProfileViewEvent.Recompose)
            }.catch {
                if (it is LoginException) {
                    logoutLocal(viewModelScope)
                    _viewEvents.setEvent(ProfileViewEvent.Logout)
                }
                // 非网络问题弹出提示
                if (it !is ConnectException && it !is SocketTimeoutException && it !is HttpException)
                    _viewEvents.setEvent(ProfileViewEvent.ShowToast(it.message!!))
            }.flowOn(Dispatchers.IO).collect()
        }
    }

    private suspend fun logoutLogic() {
        val token = AppContext.profile?.token!!
        // 避免无网络连接(直接本地先清理)
        AppContext.profile = null
        // 移除持久化
        GlobalDataBase.database.profileDao().deleteAll()
        when (val result = repository.logout(token)) {
            is NetworkState.Error -> throw result.e ?: Exception(result.msg)
            else -> {
                _viewStates.setState { copy(resList = listOf(), topicList = listOf()) }
                GlobalDataBase.database.resDao().deleteAll()
                GlobalDataBase.database.topicDao().deleteAll()
            }
        }
    }

    private fun changeUserImg(path: String, avatarMode: Boolean) {
        viewModelScope.launch {
            flow {
                if (avatarMode)
                    changeAvatarLogic(path)
                else
                    changeBackgroundLogic(path)
                emit("更改成功")
            }.onStart {
                _viewEvents.setEvent(ProfileViewEvent.ShowLoadingDialog)
            }.onEach {
                _viewEvents.setEvent(ProfileViewEvent.Recompose)
            }.catch {
                if (it is LoginException) {
                    logoutLocal(viewModelScope)
                    _viewEvents.setEvent(ProfileViewEvent.Logout)
                }
                _viewEvents.setEvent(ProfileViewEvent.ShowToast(it.message!!))
            }.onCompletion {
                _viewEvents.setEvent(ProfileViewEvent.DismissLoadingDialog)
            }.flowOn(Dispatchers.IO).collect()
        }
    }

    private suspend fun changeAvatarLogic(path: String) {
        val file = File(path)
        val id = AppContext.profile?.id!!
        Logger.d("file size: ${file.length()}")
        if (file.length() > 0) {
            when (val result = repository.changeAvatar(file, id)) {
                is NetworkState.Success -> {
                    result.data?.let { avatar ->
                        AppContext.profile = AppContext.profile?.copy(avatar = avatar)
                        // room持久化
                        GlobalDataBase.database.profileDao().deleteAll()
                        GlobalDataBase.database.profileDao().insert(AppContext.profile!!)
                    }
                }
                is NetworkState.Error -> throw result.e ?: Exception(result.msg)
            }
        } else throw Exception("获取文件失败")
    }

    private suspend fun changeBackgroundLogic(path: String) {
        val file = File(path)
        val id = AppContext.profile?.id!!
        Logger.d("file size: ${file.length()}")
        if (file.length() > 0) {
            when (val result = repository.changBackground(file, id)) {
                is NetworkState.Success -> {
                    result.data?.let { bg ->
                        AppContext.profile = AppContext.profile?.copy(background = bg)
                        // room持久化
                        GlobalDataBase.database.profileDao().deleteAll()
                        GlobalDataBase.database.profileDao().insert(AppContext.profile!!)
                    }
                }
                is NetworkState.Error -> throw result.e ?: Exception(result.msg)
            }
        } else throw Exception("获取文件失败")
    }

    private fun changeNickname(nickname: String) {
        viewModelScope.launch {
            flow {
                changeNicknameLogic(nickname)
                emit("修改成功")
            }.onStart {
                _viewEvents.setEvent(ProfileViewEvent.ShowLoadingDialog)
            }.onEach {
                _viewEvents.setEvent(ProfileViewEvent.Recompose)
            }.catch {
                if (it is LoginException) {
                    logoutLocal(viewModelScope)
                    _viewEvents.setEvent(ProfileViewEvent.Logout)
                }
                _viewEvents.setEvent(ProfileViewEvent.ShowToast(it.message!!))
            }.onCompletion {
                _viewEvents.setEvent(ProfileViewEvent.DismissLoadingDialog)
            }.flowOn(Dispatchers.IO).collect()
        }
    }

    private suspend fun changeNicknameLogic(nickname: String) {
        val id = AppContext.profile?.id!!
        when (val result = repository.changeNickname(id = id, nickname = nickname)) {
            is NetworkState.Success -> {
                AppContext.profile = AppContext.profile?.copy(nickname = nickname)
                // room持久化
                GlobalDataBase.database.profileDao().deleteAll()
                GlobalDataBase.database.profileDao().insert(AppContext.profile!!)
            }
            is NetworkState.Error -> throw result.e ?: Exception(result.msg)
        }
    }

    private fun changeTags(tags: List<String>) {
        viewModelScope.launch {
            flow {
                changeTagsLogic(tags)
                emit("修改成功")
            }.onStart {
                _viewEvents.setEvent(ProfileViewEvent.ShowLoadingDialog)
            }.onEach {
                _viewEvents.setEvent(ProfileViewEvent.Recompose)
            }.catch {
                if (it is LoginException) {
                    logoutLocal(viewModelScope)
                    _viewEvents.setEvent(ProfileViewEvent.Logout)
                }
                _viewEvents.setEvent(ProfileViewEvent.ShowToast(it.message!!))
            }.onCompletion {
                _viewEvents.setEvent(ProfileViewEvent.DismissLoadingDialog)
            }.flowOn(Dispatchers.IO).collect()
        }
    }

    private suspend fun changeTagsLogic(tags: List<String>) {
        val id = AppContext.profile?.id!!
        when (val result = repository.changeTags(id = id, tags = tags)) {
            is NetworkState.Success -> {
                AppContext.profile = AppContext.profile?.copy(tags = tags)
                // room持久化
                GlobalDataBase.database.profileDao().deleteAll()
                GlobalDataBase.database.profileDao().insert(AppContext.profile!!)
            }
            is NetworkState.Error -> throw result.e ?: Exception(result.msg)
        }
    }

    private fun getUserPost() {
        // 每5min刷新一次数据
        if (!AppContext.profile?.id.isNullOrBlank()
            && System.currentTimeMillis() - ((AppContext.profile?.lastUpdateTime)
                ?: 0L) > 5 * 60 * 1_000
        )
            viewModelScope.launch {
                flow {
                    getUserTopic()
                    getUserRes()
                    emit("获取成功")
                }.catch {
                    _viewEvents.setEvent(ProfileViewEvent.ShowToast(it.message!!))
                }.flowOn(Dispatchers.IO).collect()
            }
        else {
            viewModelScope.launch(Dispatchers.IO) {
                _viewStates.setState {
                    copy(
                        topicList = GlobalDataBase.database.topicDao().getAll() ?: listOf(),
                        resList = GlobalDataBase.database.resDao().getAll() ?: listOf()
                    )
                }
            }
        }
    }

    private suspend fun getUserTopic() {
        when (val result = repository.getUserTopic()) {
            is NetworkState.Success -> {
                _viewStates.setState { copy(topicList = result.data!!) }
                AppContext.profile =
                    AppContext.profile?.copy(lastUpdateTime = System.currentTimeMillis())
                // room持久化
                GlobalDataBase.database.topicDao().deleteAll()
                GlobalDataBase.database.topicDao().insertAll(viewStates.value.topicList)
            }
            is NetworkState.Error -> throw result.e ?: Exception(result.msg)
        }
    }

    private suspend fun getUserRes() {
        when (val result = repository.getUserRes()) {
            is NetworkState.Success -> {
                _viewStates.setState { copy(resList = result.data!!) }
                AppContext.profile =
                    AppContext.profile?.copy(lastUpdateTime = System.currentTimeMillis())
                // room持久化
                GlobalDataBase.database.resDao().deleteAll()
                GlobalDataBase.database.resDao().insertAll(viewStates.value.resList)
            }
            is NetworkState.Error -> throw result.e ?: Exception(result.msg)
        }
    }

    private fun delPost(topicId: String) {
        viewModelScope.launch {
            flow {
                delPostLogic(topicId)
                emit("删除成功")
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

    private suspend fun delPostLogic(topicId: String) {
        when (val result = TopicRepository.getInstance().delPost(topicId)) {
            is NetworkState.Success -> {
                val list = viewStates.value.topicList.filter { it.topicId != topicId }
                _viewStates.setState { copy(topicList = list) }
                // room 持久化
                GlobalDataBase.database.resDao().delete(topicId)
            }
            is NetworkState.Error -> throw result.e ?: Exception(result.msg)
        }
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
    data class ChangeNickname(val name: String) : ProfileViewAction()
    data class ChangeTags(val tags: List<String>) : ProfileViewAction()
    object GetUserPost : ProfileViewAction()
    data class DelPost(val topicId: String) : ProfileViewAction()
}

sealed class ProfileViewEvent {
    object ShowLoadingDialog : ProfileViewEvent()
    object DismissLoadingDialog : ProfileViewEvent()
    object Recompose : ProfileViewEvent()
    data class ShowToast(val msg: String) : ProfileViewEvent()
    object Logout : ProfileViewEvent()
}