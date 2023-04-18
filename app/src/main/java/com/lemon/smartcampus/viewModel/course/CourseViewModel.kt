package com.lemon.smartcampus.viewModel.course

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lemon.smartcampus.data.database.database.GlobalDataBase
import com.lemon.smartcampus.data.database.entities.CourseEntity
import com.orhanobut.logger.Logger
import com.zj.mvi.core.SharedFlowEvents
import com.zj.mvi.core.setEvent
import com.zj.mvi.core.setState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.stream.Stream

class CourseViewModel : ViewModel() {

    private val _viewStates = MutableStateFlow(CourseViewState())
    val viewStates = _viewStates.asStateFlow()
    private val _viewEvents = SharedFlowEvents<CourseViewEvent>()
    val viewEvents = _viewEvents.asSharedFlow()

    fun dispatch(viewAction: CourseViewAction) {
        when (viewAction) {
            is CourseViewAction.UpdateDInWeek -> _viewStates.setState { copy(dInWeek = viewAction.dInWeek) }
            is CourseViewAction.UpdateWeek -> _viewStates.setState { copy(week = viewAction.week) }
            is CourseViewAction.GetOneDayCourse -> getOneDayCourse()
        }
    }

    private fun getOneDayCourse() {
        val dInWeek = viewStates.value.dInWeek
        val week = viewStates.value.week
        viewModelScope.launch {
            flow {
                emit(GlobalDataBase.database.courseDao().getOneDay(dInWeek, week))
            }.onEach {
                Logger.d("getting: $it")
                it?.let {
                    _viewStates.setState { copy(oneDayCourse = it) }
                    _viewEvents.setEvent(CourseViewEvent.Recompose)
                }
            }.flowOn(Dispatchers.IO).collect()
        }
    }

}

data class CourseViewState(
    val oneDayCourse: List<CourseEntity> = listOf(),
    val dInWeek: String = "一",
    val week: Int = 1
)

sealed class CourseViewAction {
    data class UpdateDInWeek(val dInWeek: String) : CourseViewAction()
    data class UpdateWeek(val week: Int) : CourseViewAction()
    object GetOneDayCourse : CourseViewAction()
}

sealed class CourseViewEvent {
    object Recompose : CourseViewEvent()
}