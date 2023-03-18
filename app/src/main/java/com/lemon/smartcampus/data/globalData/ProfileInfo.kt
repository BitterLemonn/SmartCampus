package com.lemon.smartcampus.data.globalData

import com.lemon.smartcampus.data.entities.ProfileEntity

object ProfileInfo {
    var profile: ProfileEntity? = null
        get() = field ?: ProfileEntity("", "", "", "", listOf())
}