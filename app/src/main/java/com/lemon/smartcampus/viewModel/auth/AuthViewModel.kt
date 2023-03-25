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
            is AuthViewAction.CheckForgetCode -> checkForgetToken()
            is AuthViewAction.ChangePassword -> changePassword()
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
                Logger.d(result.data)
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

    private fun checkForgetToken() {
        viewModelScope.launch {
            flow {
                _viewStates.setState { copy(id = "") }
                checkForgetTokenLogic()
                emit("验证码正确")
            }.onStart {
                _viewEvents.setEvent(AuthViewEvent.ShowLoadingDialog)
            }.onEach {
                if (_viewStates.value.id.isNotBlank())
                    _viewEvents.setEvent(AuthViewEvent.CheckSuccess)
            }.catch {
                _viewEvents.setEvent(AuthViewEvent.ShowToast(it.message!!))
            }.onCompletion {
                _viewEvents.setEvent(AuthViewEvent.DismissLoadingDialog)
            }.flowOn(Dispatchers.IO).collect()
        }
    }

    private suspend fun checkForgetTokenLogic() {
        val phone = viewStates.value.phone
        val code = viewStates.value.token
        if (phone.length < 11) throw Exception("请输入正确的大陆手机号码")
        if (code.length < 4) throw Exception("请输入正确的验证码")
        when (val result = repository.checkForgetToken(phone = phone, code = code)) {
            is NetworkState.Error -> throw Exception(result.msg)
            is NetworkState.Success -> _viewStates.setState { copy(id = result.data!!) }
        }
    }

    private fun changePassword() {
        viewModelScope.launch {
            flow {
                changePasswordLogic()
                emit("修改成功")
            }.catch {
                _viewEvents.setEvent(AuthViewEvent.ShowToast(it.message!!))
            }.onCompletion {
                _viewEvents.setEvent(AuthViewEvent.DismissLoadingDialog)
            }.flowOn(Dispatchers.IO).collect()
        }
    }

    private suspend fun changePasswordLogic() {
        val userId = viewStates.value.id
        val password = viewStates.value.password
        if (password.length < 6) throw Exception("密码长度不得小于6位")
        _viewEvents.setEvent(AuthViewEvent.ShowLoadingDialog)
        if (userId.isBlank())
            throw Exception("获取id失败")
        when (val result = repository.changePassword(password = md5(password), userId = userId)) {
            is NetworkState.Success -> _viewEvents.setEvent(AuthViewEvent.ChangeSuccess)
            is NetworkState.Error -> throw result.e ?: Exception(result.msg)
        }
    }
}

data class AuthViewState(
    val phone: String = "",
    val password: String = "",
    val token: String = "",
    val id: String = ""
)

sealed class AuthViewAction {
    data class UpdatePhone(val phone: String) : AuthViewAction()
    data class UpdatePassword(val password: String) : AuthViewAction()
    data class UpdateToken(val token: String) : AuthViewAction()

    object LoginWithPassword : AuthViewAction()
    object LoginWithToken : AuthViewAction()
    object Register : AuthViewAction()
    object RequestToken : AuthViewAction()
    object CheckForgetCode : AuthViewAction()
    object ChangePassword : AuthViewAction()
}

sealed class AuthViewEvent {
    object ShowLoadingDialog : AuthViewEvent()
    object DismissLoadingDialog : AuthViewEvent()
    object TransIntent : AuthViewEvent()
    object AlreadyRequestCode : AuthViewEvent()
    object CheckSuccess : AuthViewEvent()
    object ChangeSuccess : AuthViewEvent()
    data class ShowToast(val msg: String) : AuthViewEvent()
}