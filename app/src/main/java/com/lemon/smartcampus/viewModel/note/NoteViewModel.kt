package com.lemon.smartcampus.viewModel.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lemon.smartcampus.data.database.database.GlobalDataBase
import com.lemon.smartcampus.data.database.entities.NoteEntity
import com.orhanobut.logger.Logger
import com.zj.mvi.core.SharedFlowEvents
import com.zj.mvi.core.setEvent
import com.zj.mvi.core.setState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class NoteViewModel : ViewModel() {
    private val _viewStates = MutableStateFlow(NoteViewState())
    val viewStates = _viewStates.asStateFlow()
    private val _viewEvents = SharedFlowEvents<NoteViewEvent>()
    val viewEvents = _viewEvents.asSharedFlow()

    fun dispatch(viewAction: NoteViewAction) {
        when (viewAction) {
            is NoteViewAction.UpdateTitle -> _viewStates.setState { copy(title = viewAction.title) }
            is NoteViewAction.UpdateContent -> _viewStates.setState { copy(content = viewAction.content) }
            is NoteViewAction.UpdateEntity -> {
                _viewStates.setState {
                    copy(
                        selectedEntity = viewAction.entity,
                        title = viewAction.entity.title,
                        content = viewAction.entity.content
                    )
                }
            }
            is NoteViewAction.GetNoteList -> getNoteList()
            is NoteViewAction.PostNote -> postNote()
            is NoteViewAction.DelNote -> delNote()
        }
    }

    private fun getNoteList() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = GlobalDataBase.database.noteDao().getAll()
            if (!list.isNullOrEmpty()) _viewStates.setState { copy(noteList = list) }
        }
    }

    private fun postNote() {
        viewModelScope.launch {
            if (viewStates.value.content.isNotBlank() || viewStates.value.title.isNotBlank()) {
                flow {
                    val entity = viewStates.value.selectedEntity
                    val time = SimpleDateFormat(
                        "yyyy年MM月dd日 hh:mm:ss",
                        Locale.CHINA
                    ).format(System.currentTimeMillis())
                    val noteEntity = NoteEntity(
                        id = entity.id.ifBlank { System.currentTimeMillis().toString() },
                        title = viewStates.value.title,
                        content = viewStates.value.content,
                        createTime = entity.createTime.ifBlank { time }
                    )
                    val list = viewStates.value.noteList.toMutableList()
                    if (entity.id.isBlank()) list.add(noteEntity)
                    else list.replaceAll { if (it.id == entity.id) noteEntity else it }
                    _viewStates.setState { copy(noteList = list) }
                    // 更新当前state
                    emit(noteEntity)
                }.onEach {
                    // 持久化
                    GlobalDataBase.database.noteDao().insert(it)
                    Logger.d("写入数据库: $it")
                    // 清除state
                    _viewStates.setState {
                        copy(title = "", content = "", selectedEntity = NoteEntity())
                    }
                }.catch {
                    _viewEvents.setEvent(NoteViewEvent.ShowToast(it.message ?: "未知错误, 请联系管理员"))
                }.flowOn(Dispatchers.IO).collect()
            }
        }
    }

    private fun delNote() {
        val entity = viewStates.value.selectedEntity
        if (entity.id.isNotBlank()) {
            viewModelScope.launch(Dispatchers.IO) {
                GlobalDataBase.database.noteDao().delete(entity.id)
                val list = viewStates.value.noteList.toMutableList()
                list.remove(entity)
                _viewStates.setState {
                    copy(
                        noteList = list,
                        selectedEntity = NoteEntity(),
                        title = "",
                        content = ""
                    )
                }
            }
        }
    }
}

data class NoteViewState(
    val noteList: List<NoteEntity> = listOf(),
    val title: String = "",
    val content: String = "",
    val selectedEntity: NoteEntity = NoteEntity()
)

sealed class NoteViewAction {
    data class UpdateTitle(val title: String) : NoteViewAction()
    data class UpdateContent(val content: String) : NoteViewAction()
    data class UpdateEntity(val entity: NoteEntity) : NoteViewAction()
    object GetNoteList : NoteViewAction()
    object PostNote : NoteViewAction()
    object DelNote : NoteViewAction()
}

sealed class NoteViewEvent {
    data class ShowToast(val msg: String, val success: Boolean = false) : NoteViewEvent()
}