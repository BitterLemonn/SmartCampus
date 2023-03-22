package com.lemon.smartcampus.viewModel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lemon.smartcampus.data.database.database.GlobalDataBase
import com.lemon.smartcampus.data.globalData.AppContext
import com.lemon.smartcampus.data.repository.AuthRepository
import com.lemon.smartcampus.utils.NetworkState
import com.lemon.smartcampus.utils.md5
import com.orhanobut.logger.Logger
import com.zj.mvi.core.SharedFlowEvents
import com.zj.mvi.core.setEvent
import com.zj.mvi.core.setState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository.getInstance()

    private val _viewStates = MutableStateFlow(AuthViewState())
    val viewStates = _viewStates.asStateFlow()
    private val _viewEvents = SharedFlowEvents<AuthViewEvent>()
    val viewEvents = _viewEvents.asSharedFlow()

    fun dispatch(viewAction: AuthViewAction) {
        when (viewAction) {
            is AuthViewAction.UpdatePhone -> _viewStates.setState { copy(phone = viewAction.phone) }
            is AuthViewAction.UpdatePassword -> _viewStates.setState { copy(password = viewAction.password) }
            is AuthViewAction.UpdateToken -> _viewStates.setState { copy(token = viewAction.token) }
            is AuthViewAction.LoginWithPassword -> loginWithPassword()
            is AuthViewAction.LoginWithToken -> loginWithToken()
            is AuthViewAction.Register -> register()
            is AuthViewAction.RequestToken -> requestToken()
        }
    }

    private fun loginWithPassword() {
        viewModelScope.launch {
            flow {
                loginWithPasswordLogic()
                emit("登陆成功")
            }.onEach {
                _viewEvents.setEvent(
                    AuthViewEvent.DismissLoadingDialog,
                    AuthViewEvent.TransIntent
                )
            }.catch {
                _viewEvents.setEvent(
                    AuthViewEvent.ShowToast(it.message!!),
                    AuthViewEvent.DismissLoadingDialog
                )
            }.flowOn(Dispatchers.IO).collect()
        }
    }

    private suspend fun loginWithPasswordLogic() {
        val phone = viewStates.value.phone
        val password = md5(viewStates.value.password)
        if (phone.length < 11) throw Exception("请输入正确的大陆手机号码")
        _viewEvents.setEvent(AuthViewEvent.ShowLoadingDialog)

        when (val result = repository.loginWithPassword(phone, password)) {
            is NetworkState.Success -> {
                // 存入全局变量
                AppContext.profile = result.data
                // Room持久化
                GlobalDataBase.database.profileDao().deleteAll()
                GlobalDataBase.database.profileDao().insert(AppContext.profile!!)
            }
            is NetworkState.Error -> throw Exception(result.msg)
        }
    }

    private fun loginWithToken() {
        viewModelScope.launch {
            flow {
                loginWithTokenLogic()
                emit("登陆成功")
            }.onEach {
                _viewEvents.setEvent(
                    AuthViewEvent.DismissLoadingDialog,
                    AuthViewEvent.TransIntent
                )
            }.catch {
                _viewEvents.setEvent(
                    AuthViewEvent.ShowToast(it.message!!),
                    AuthViewEvent.DismissLoadingDialog
                )
            }.flowOn(Dispatchers.IO).collect()
        }
    }

    private suspend fun loginWithTokenLogic() {
        val token = viewStates.value.token
        val phone = viewStates.value.phone
        if (phone.length < 11) throw Exception("请输入正确的大陆手机号码")
        _viewEvents.setEvent(AuthViewEvent.ShowLoadingDialog)

        when (val result = repository.loginWithToken(phone, token)) {
            is NetworkState.Success -> {
                // 存入全局变量
                AppContext.profile = result.data
                // Room持久化
                GlobalDataBase.database.profileDao().deleteAll()
                GlobalDataBase.database.profileDao().insert(AppContext.profile!!)
            }
            is NetworkState.Error -> throw Exception(result.msg)
        }
    }

    private fun register() {
        viewModelScope.launch {
            flow {
                registerLogic()
                emit("注册成功")
            }.onEach {
                _viewEvents.setEvent(
                    AuthViewEvent.DismissLoadingDialog,
                    AuthViewEvent.TransIntent
                )
            }.catch {
                _viewEvents.setEvent(
                    AuthViewEvent.ShowToast(it.message!!),
                    AuthViewEvent.DismissLoadingDialog
                )
            }.flowOn(Dispatchers.IO).collect()
        }
    }

    private suspend fun registerLogic() {
        val token = viewStates.value.token
        val phone = viewStates.value.phone
        val password = md5(viewStates.value.password)
        if (phone.length < 11) throw Exception("请输入正确的大陆手机号码")
        if (password.length < 6) throw Exception("密码需要大于6位")

        _viewEvents.setEvent(AuthViewEvent.ShowLoadingDialog)
        when (val result = repository.register(phone, password, token)) {
            is NetworkState.Success -> {
                // 存入全局变量
                AppContext.profile = result.data
                // Room持久化
                GlobalDataBase.database.profileDao().deleteAll()
                GlobalDataBase.database.profileDao().insert(AppContext.profile!!)
            }
            is NetworkState.Error -> throw Exception(result.msg)
        }
    }

    private fun requestToken() {
        viewModelScope.launch {
            flow {
                requestTokenLogic()
                emit("请求成功")
            }.onEach {
                _viewEvents.setEvent(AuthViewEvent.AlreadyRequestCode)
            }.catch {
                _viewEvents.setEvent(AuthViewEvent.ShowToast(it.message!!))
            }.flowOn(Dispatchers.IO).collect()
        }
    }

    private suspend fun requestTokenLogic() {
        val phone = viewStates.value.phone
        if (phone.length < 11) throw Exception("请输入正确的大陆手机号码")
        when (val result = repository.requestToken(phone)) {
            is NetworkState.Error -> throw Exception(result.msg)
            else -> {}
        }
    }
}

data class AuthViewState(
    val phone: String = "",
    val password: String = "",
    val token: String = "",
)

sealed class AuthViewAction {
    data class UpdatePhone(val phone: String) : AuthViewAction()
    data class UpdatePassword(val password: String) : AuthViewAction()
    data class UpdateToken(val token: String) : AuthViewAction()

    object LoginWithPassword : AuthViewAction()
    object LoginWithToken : AuthViewAction()
    object Register : AuthViewAction()
    object RequestToken : AuthViewAction()
}

sealed class AuthViewEvent {
    object ShowLoadingDialog : AuthViewEvent()
    object DismissLoadingDialog : AuthViewEvent()
    object TransIntent : AuthViewEvent()
    object AlreadyRequestCode : AuthViewEvent()
    data class ShowToast(val msg: String) : AuthViewEvent()
}