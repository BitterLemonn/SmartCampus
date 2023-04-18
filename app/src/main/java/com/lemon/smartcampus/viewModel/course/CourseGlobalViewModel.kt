package com.lemon.smartcampus.viewModel.course

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lemon.smartcampus.data.database.database.GlobalDataBase
import com.lemon.smartcampus.data.database.entities.CourseGlobalSettingEntity
import com.lemon.smartcampus.data.globalData.AppContext
import com.orhanobut.logger.Logger
import com.zj.mvi.core.SharedFlowEvents
import com.zj.mvi.core.setEvent
import com.zj.mvi.core.setState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CourseGlobalViewModel : ViewModel() {

    private val _viewStates = MutableStateFlow(CourseGlobalViewState())
    val viewStates = _viewStates.asStateFlow()
    private val _viewEvents = SharedFlowEvents<CourseGlobalViewEvent>()
    val viewEvents = _viewEvents.asSharedFlow()

    fun dispatch(viewAction: CourseGlobalViewAction) {
        when (viewAction) {
            is CourseGlobalViewAction.UpdateTotalWeek -> _viewStates.setState { copy(totalWeek = viewAction.totalWeek) }
            is CourseGlobalViewAction.UpdateSingleTime -> _viewStates.setState { copy(singleTime = viewAction.singleTime) }
            is CourseGlobalViewAction.UpdateMorningClass -> _viewStates.setState { copy(morningClass = viewAction.morningClass) }
            is CourseGlobalViewAction.UpdateNoonClass -> _viewStates.setState { copy(noonClass = viewAction.noonClass) }
            is CourseGlobalViewAction.UpdateNightClass -> _viewStates.setState { copy(nightClass = viewAction.nightClass) }
            is CourseGlobalViewAction.UpdateBreakTime -> _viewStates.setState { copy(breakTime = viewAction.breakTime) }
            is CourseGlobalViewAction.UpdateNoonBreakTime -> _viewStates.setState {
                copy(noonBreakTime = viewAction.noonBreakTime)
            }
            is CourseGlobalViewAction.UpdateNightBreakTime -> _viewStates.setState {
                copy(nightBreakTime = viewAction.nightBreakTime)
            }
            is CourseGlobalViewAction.LoadSetting -> loadFromCache()
            is CourseGlobalViewAction.SaveSetting -> saveGlobalSetting()
            is CourseGlobalViewAction.UpdateClassTimeMap -> updateClassTimeMap()
            is CourseGlobalViewAction.UpdateStartTime -> _viewStates.setState { copy(startTime = viewAction.startTime) }
            is CourseGlobalViewAction.UpdateEditClassIndex -> updateEditClassIndex(viewAction.editClassIndex)
            is CourseGlobalViewAction.UpdateEndTime -> _viewStates.setState { copy(endTime = viewAction.endTime) }
        }
    }

    private fun loadFromCache() {
        val setting = AppContext.courseGlobal
        _viewStates.setState {
            copy(
                totalWeek = if (setting.totalWeek == -1) null else setting.totalWeek,
                singleTime = if (setting.singleTime == -1) null else setting.singleTime,
                morningClass = if (setting.morningClass == -1) null else setting.morningClass,
                noonClass = if (setting.noonClass == -1) null else setting.noonClass,
                nightClass = if (setting.nightClass == -1) null else setting.nightClass,
                breakTime = if (setting.breakTime == -1) null else setting.breakTime,
                noonBreakTime = if (setting.noonBreakTime == -1) null else setting.noonBreakTime,
                nightBreakTime = if (setting.nightBreakTime == -1) null else setting.nightBreakTime,
                weekendClass = setting.weekendClass,
                classTimeMap = mapOf(Pair(0, "7:00 - 7:01"))
            )
        }
    }

    private fun saveGlobalSetting() {
        viewModelScope.launch {
            flow {
                if ((viewStates.value.totalWeek ?: 0) <= 0)
                    throw Exception("学期周数不能小于1")
                if ((viewStates.value.singleTime ?: 0) <= 0)
                    throw Exception("单节课时长不能小于1")
                emit(
                    CourseGlobalSettingEntity(
                        totalWeek = viewStates.value.totalWeek ?: 1,
                        singleTime = viewStates.value.singleTime ?: 1,
                        morningClass = viewStates.value.morningClass ?: 0,
                        noonClass = viewStates.value.noonClass ?: -1,
                        nightClass = viewStates.value.nightClass ?: -1,
                        breakTime = viewStates.value.nightBreakTime ?: 0,
                        noonBreakTime = viewStates.value.noonBreakTime ?: 0,
                        nightBreakTime = viewStates.value.nightBreakTime ?: 0,
                        weekendClass = viewStates.value.weekendClass
                    )
                )
            }.catch {
                _viewEvents.setEvent(CourseGlobalViewEvent.ShowToast(it.message ?: "未知错误,请联系管理员"))
            }.onEach {
                // 持久化
                GlobalDataBase.database.courseGlobalDao().deleteAll()
                GlobalDataBase.database.courseGlobalDao().insert(it)
                AppContext.courseGlobal = it

                _viewEvents.setEvent(CourseGlobalViewEvent.TransIntent)
            }.flowOn(Dispatchers.IO).collect()
        }
    }

    private fun updateClassTimeMap() {
        val classIndex = viewStates.value.editClassIndex
        val time = "${viewStates.value.startTime} - ${viewStates.value.endTime}"
        viewModelScope.launch {
            if (time.matches("^(20|21|22|23|[0-1]\\d):[0-5]\\d - (20|21|22|23|[0-1]\\d):[0-5]\\d".toRegex())) {
                val map = viewStates.value.classTimeMap.toMutableMap()
                map[classIndex] = time
                _viewStates.setState { copy(classTimeMap = map) }
                _viewEvents.setEvent(CourseGlobalViewEvent.CanNext)
                Logger.d("map: $map")
            } else
                _viewEvents.setEvent(CourseGlobalViewEvent.ShowToast("时间格式错误,请输入正确的时间"))
        }
    }

    private fun updateEditClassIndex(editClassIndex: Int) {
        val startTime: String
        val endTime: String
        val time = viewStates.value.classTimeMap.getOrDefault(editClassIndex - 1, "")
        if (time.isBlank()) {
            val firstClass = viewStates.value.classTimeMap.getOrDefault(1, "7:00 - 7:00")
            val firstEndList = firstClass.split(" - ").last().split(":")
            val firstEnd = firstEndList.first().toInt() * 60 + firstEndList.last().toInt()

            val space = if (editClassIndex <= viewStates.value.morningClass!!)
                viewStates.value.breakTime!! + (editClassIndex - 1) *
                        (viewStates.value.breakTime!! + viewStates.value.singleTime!!)
            else if (editClassIndex in viewStates.value.morningClass!!..(viewStates.value.noonClass
                    ?: 0)
            )
                viewStates.value.breakTime!! + (editClassIndex - 1) *
                        (viewStates.value.breakTime!! + viewStates.value.singleTime!!) + viewStates.value.noonBreakTime!!
            else if (editClassIndex > viewStates.value.morningClass!! + (viewStates.value.noonClass
                    ?: 0)
            ) viewStates.value.breakTime!! + (editClassIndex - 1) *
                    (viewStates.value.breakTime!! + viewStates.value.singleTime!!) +
                    viewStates.value.noonBreakTime!! + viewStates.value.nightBreakTime!!
            else 0
            val start = firstEnd + space
            val end = start + (viewStates.value.singleTime ?: 1)
            startTime = changeIntTimeToString(start)
            endTime = changeIntTimeToString(end)
        } else {
            val lastClassEndList = time.split(" - ").last().split(":")
            val lastClassEnd =
                lastClassEndList.first().toInt() * 60 + lastClassEndList.last().toInt()
            val start = lastClassEnd + (viewStates.value.singleTime ?: 1)
            val end = start + (viewStates.value.singleTime ?: 1)
            startTime = changeIntTimeToString(start)
            endTime = changeIntTimeToString(end)
        }
        _viewStates.setState { copy(editClassIndex = editClassIndex, startTime = startTime, endTime = endTime) }
    }

    private fun changeIntTimeToString(intTime: Int): String {
        return "${if (intTime / 60 < 10) "0${intTime / 60}" else intTime / 60}:${if (intTime % 60 < 10) "0${intTime % 60}" else intTime % 60}"
    }
}

data class CourseGlobalViewState(
    val totalWeek: Int? = null,
    val singleTime: Int? = null,
    val morningClass: Int? = null,
    val noonClass: Int? = null,
    val nightClass: Int? = null,
    val breakTime: Int? = null,
    val noonBreakTime: Int? = null,
    val nightBreakTime: Int? = null,
    val weekendClass: Boolean = false,
    val classTimeMap: Map<Int, String> = mapOf(),
    val editClassIndex: Int = 1,
    val startTime: String = "7:00",
    val endTime: String = "7:00"
)

sealed class CourseGlobalViewAction {
    data class UpdateTotalWeek(val totalWeek: Int?) : CourseGlobalViewAction()
    data class UpdateSingleTime(val singleTime: Int?) : CourseGlobalViewAction()
    data class UpdateMorningClass(val morningClass: Int?) : CourseGlobalViewAction()
    data class UpdateNoonClass(val noonClass: Int?) : CourseGlobalViewAction()
    data class UpdateNightClass(val nightClass: Int?) : CourseGlobalViewAction()
    data class UpdateBreakTime(val breakTime: Int?) : CourseGlobalViewAction()
    data class UpdateNoonBreakTime(val noonBreakTime: Int?) : CourseGlobalViewAction()
    data class UpdateNightBreakTime(val nightBreakTime: Int?) : CourseGlobalViewAction()
    object LoadSetting : CourseGlobalViewAction()
    object SaveSetting : CourseGlobalViewAction()
    object UpdateClassTimeMap : CourseGlobalViewAction()
    data class UpdateEditClassIndex(val editClassIndex: Int) : CourseGlobalViewAction()
    data class UpdateStartTime(val startTime: String) : CourseGlobalViewAction()
    data class UpdateEndTime(val endTime: String) : CourseGlobalViewAction()
}

sealed class CourseGlobalViewEvent {
    data class ShowToast(val msg: String) : CourseGlobalViewEvent()
    object TransIntent : CourseGlobalViewEvent()
    object CanNext : CourseGlobalViewEvent()
}