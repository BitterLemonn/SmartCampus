package com.lemon.smartcampus.data.globalData

import com.lemon.smartcampus.data.database.entities.ProfileEntity
import com.lemon.smartcampus.data.database.entities.TopicEntity

object AppContext {
    var profile: ProfileEntity? = null
        get() = field ?: ProfileEntity("", "", "", "", "", "", listOf())
    var topicDetail: Map<String, TopicEntity> = mapOf()
}