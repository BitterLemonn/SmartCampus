package com.lemon.smartcampus.data.globalData

import com.lemon.smartcampus.data.database.entities.CourseGlobalSettingEntity
import com.lemon.smartcampus.data.database.entities.ProfileEntity
import com.lemon.smartcampus.data.database.entities.TopicEntity

object AppContext {
    var profile: ProfileEntity? = null
        get() = field ?: ProfileEntity("", "", "", "", "", "", listOf(), mutableMapOf())
    var courseGlobal: CourseGlobalSettingEntity =
        CourseGlobalSettingEntity(
            totalWeek = -1,
            singleTime = -1,
            morningClass = -1,
            noonBreakTime = -1,
            noonClass = -1,
            breakTime = -1,
            nightBreakTime = -1,
            nightClass = -1,
            weekendClass = false
        )
    var topicDetail: Map<String, TopicEntity> = mapOf()
    var downloadedFile: Map<String, String> = mapOf()
}