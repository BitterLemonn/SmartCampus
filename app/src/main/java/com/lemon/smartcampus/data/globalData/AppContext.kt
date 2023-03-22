package com.lemon.smartcampus.data.globalData

import com.lemon.smartcampus.data.database.entities.ProfileEntity

object AppContext {
    var profile: ProfileEntity? = null
        get() = field ?: ProfileEntity("", "", "", "", "", "", listOf())
}