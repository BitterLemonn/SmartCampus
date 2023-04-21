package com.lemon.smartcampus.viewModel.character

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lemon.smartcampus.data.database.entities.CharacterEntity
import com.lemon.smartcampus.data.repository.CharacterRepository
import com.lemon.smartcampus.utils.NetworkState
import com.lemon.smartcampus.viewModel.topic.DetailViewEvent
import com.zj.mvi.core.SharedFlowEvents
import com.zj.mvi.core.setState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CharacterViewModel : ViewModel() {
    private val repository = CharacterRepository.getInstance()

    private val _viewStates = MutableStateFlow(CharacterViewState())
    val viewStates = _viewStates.asStateFlow()
    private val _viewEvents = SharedFlowEvents<CharacterViewEvent>()
    val viewEvents = _viewEvents.asSharedFlow()

    fun dispatch(viewAction: CharacterViewAction) {
        when (viewAction) {
            is CharacterViewAction.GetPage -> getPage(isScholar = viewAction.isScholar)
        }
    }

    private fun getPage(isScholar: Boolean) {
        viewModelScope.launch {
            flow {
                getPageLogic(isScholar)
                emit("获取成功")
            }.catch {
                DetailViewEvent.ShowToast(it.message!!, false)
            }.flowOn(Dispatchers.IO).collect()
        }
    }

    private suspend fun getPageLogic(isScholar: Boolean) {
        when (val result =
            if (isScholar) repository.getScholarList() else repository.getCharacterList()) {
            is NetworkState.Error -> throw result.e ?: Exception(result.msg)
            is NetworkState.Success -> {
                val data = result.data!!
                _viewStates.setState {
                    copy(itemList = data)
                }
            }
        }
    }
}

data class CharacterViewState(
    val itemList: List<CharacterEntity> = listOf()
)

sealed class CharacterViewAction {
    data class GetPage(val isScholar: Boolean) : CharacterViewAction()
}

sealed class CharacterViewEvent {
    data class ShowToast(val msg: String, val success: Boolean) : CharacterViewEvent()
}