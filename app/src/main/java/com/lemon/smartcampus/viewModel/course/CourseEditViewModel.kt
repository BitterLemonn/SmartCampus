package com.lemon.smartcampus.viewModel.course

import android.provider.Settings.Global
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lemon.smartcampus.data.database.database.GlobalDataBase
import com.lemon.smartcampus.data.database.entities.CourseEntity
import com.lemon.smartcampus.utils.charDay2CalendarDay
import com.orhanobut.logger.Logger
import com.zj.mvi.core.SharedFlowEvents
import com.zj.mvi.core.setEvent
import com.zj.mvi.core.setState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CourseEditViewModel : ViewModel() {

    private val _viewStates = MutableStateFlow(CourseEditViewState())
    val viewStates = _viewStates.asStateFlow()
    private val _viewEvents = SharedFlowEvents<CourseEditViewEvent>()
    val viewEvents = _viewEvents.asSharedFlow()

    fun dispatch(viewAction: CourseEditViewAction) {
        when (viewAction) {
            is CourseEditViewAction.Init -> init(viewAction.entity)
            is CourseEditViewAction.UpdateCourseName -> _viewStates.setState { copy(courseName = viewAction.courseName) }
            is CourseEditViewAction.UpdateLocation -> _viewStates.setState { copy(location = viewAction.location) }
            is CourseEditViewAction.UpdateDInWeek -> _viewStates.setState { copy(dInWeek = viewAction.dInWeek) }
            is CourseEditViewAction.UpdateStartWeek -> _viewStates.setState { copy(startWeek = viewAction.startWeek) }
            is CourseEditViewAction.UpdateEndWeek -> _viewStates.setState { copy(endWeek = viewAction.endWeek) }
            is CourseEditViewAction.UpdateStartTime -> _viewStates.setState { copy(startTime = viewAction.startTime) }
            is CourseEditViewAction.UpdateEndTime -> _viewStates.setState { copy(endTime = viewAction.endTime) }
            is CourseEditViewAction.UpdateAlarmTime ->
                _viewStates.setState {
                    copy(
                        alarmTime = viewAction.alarmTime,
                        isAlarm = viewAction.alarmTime > 0
                    )
                }
            is CourseEditViewAction.SaveCourse -> saveCourse(id = viewAction.id)
        }
    }

    private fun init(entity: CourseEntity){
        _viewStates.setState { copy(
            courseName = entity.name,
            location = entity.location,
            dInWeek = entity.dayInWeek,
            startWeek = entity.startWeek,
            endWeek = entity.endWeek,
            startTime = entity.startTime,
            endTime = entity.endTime,
            alarmTime = entity.alarmTime,
            isAlarm = entity.needAlarm,
            editId = entity.id
        ) }
    }

    private fun saveCourse(id: String?) {
        viewModelScope.launch {
            flow {
                if (viewStates.value.courseName.isBlank())
                    throw Exception("课程名不能为空")
                val pair = checkTime(id)
                if (pair.first) {
                    // 移除原有数据
                    GlobalDataBase.database.courseDao().delete(viewStates.value.editId)

                    val uid = pair.second
                    val courseEntity = CourseEntity(
                        startTime = viewStates.value.startTime,
                        endTime = viewStates.value.endTime,
                        startWeek = viewStates.value.startWeek,
                        endWeek = viewStates.value.endWeek,
                        dayInWeek = viewStates.value.dInWeek,
                        name = viewStates.value.courseName,
                        location = viewStates.value.location,
                        needAlarm = viewStates.value.isAlarm,
                        alarmTime = viewStates.value.alarmTime,
                        id = uid
                    )
                    emit(courseEntity)
                } else throw Exception("时间逻辑错误, 请检查")
            }.catch {
                _viewEvents.setEvent(CourseEditViewEvent.ShowToast(it.message ?: "未知错误,请联系管理员"))
            }.onEach {
                // 持久化
                Logger.d("saving: $it")
                GlobalDataBase.database.courseDao().insert(it)
                if (viewStates.value.isAlarm) {
                    val timeList = viewStates.value.startTime.split(":")
                    val time = timeList.first().toInt() * 60 + timeList.last().toInt()
                    val alarmTime = time - viewStates.value.alarmTime
                    val timeH = alarmTime / 60
                    val timeM = alarmTime % 60
                    val dayInWeek = charDay2CalendarDay(viewStates.value.dInWeek)
                    _viewEvents.setEvent(
                        CourseEditViewEvent.SetAlarm(dayInWeek, timeH, timeM),
                        CourseEditViewEvent.TransIntent
                    )
                } else
                    _viewEvents.setEvent(CourseEditViewEvent.TransIntent)
            }.flowOn(Dispatchers.IO).collect()
        }
    }

    private fun checkTime(id: String?): Pair<Boolean, String> {
        val startList = viewStates.value.startTime.split(":")
        val start = startList[0].toInt() * 3_600 + startList[1].toInt() * 60
        val endList = viewStates.value.endTime.split(":")
        val end = endList[0].toInt() * 3_600 + endList[1].toInt() * 60
        return Pair(end > start, id ?: "$start:$end:${System.currentTimeMillis()}")
    }

}

data class CourseEditViewState(
    val courseName: String = "",
    val location: String = "",
    val dInWeek: String = "一",
    val startWeek: Int = 1,
    val endWeek: Int = 1,
    val startTime: String = "7:00",
    val endTime: String = "7:00",
    val alarmTime: Int = 0,
    val isAlarm: Boolean = false,
    val editId: String = ""
)

sealed class CourseEditViewAction {
    data class Init(val entity: CourseEntity): CourseEditViewAction()
    data class UpdateCourseName(val courseName: String) : CourseEditViewAction()
    data class UpdateLocation(val location: String) : CourseEditViewAction()
    data class UpdateDInWeek(val dInWeek: String) : CourseEditViewAction()
    data class UpdateStartWeek(val startWeek: Int) : CourseEditViewAction()
    data class UpdateEndWeek(val endWeek: Int) : CourseEditViewAction()
    data class UpdateStartTime(val startTime: String) : CourseEditViewAction()
    data class UpdateEndTime(val endTime: String) : CourseEditViewAction()
    data class UpdateAlarmTime(val alarmTime: Int) : CourseEditViewAction()
    data class SaveCourse(val id: String? = null) : CourseEditViewAction()
}

sealed class CourseEditViewEvent {
    data class ShowToast(val msg: String) : CourseEditViewEvent()
    object TransIntent : CourseEditViewEvent()
    data class SetAlarm(val dayInWeek: Int, val timeH: Int, val timeM: Int) : CourseEditViewEvent()
}