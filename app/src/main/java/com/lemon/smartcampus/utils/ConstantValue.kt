package com.lemon.smartcampus.utils

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

// 导航
const val COVER_PAGE = "COVER_PAGE"
const val INTRO_PAGE = "INTRO_PAGE"
const val CALENDAR_PAGE = "CALENDAR_PAGE"
const val CHARACTER_PAGE = "CHARACTER_PAGE"
const val CHARACTER_DETAIL_PAGE = "CHARACTER_DETAIL_PAGE"
const val HOME_PAGE = "HOME_PAGE"
const val AUTH_PAGE = "AUTH_PAGE"
const val PUBLISH_PAGE = "PUBLISH_PAGE"
const val DETAILS_PAGE = "DETAILS_PAGE"
const val COURSE_PAGE = "COURSE_PAGE"
const val COURSE_EDIT_PAGE = "COURSE_EDIT_PAGE"
const val COURSE_GLOBAL_PAGE = "COURSE_GLOBAL_PAGE"
const val INFO_DETAIL = "INFO_DETAIL"
const val INFO_LIST = "INFO_LIST"
const val NOTE_PAGE = "NOTE_PAGE"

// 每页数据量
const val COUNT_PER_PAGE = 8

const val SMART_CAMPUS_CN = "智慧校园: "
const val SMART_CAMPUS = "SMART_CAMPUS"
const val INFO_REFRESH_TIME = "INFO_REFRESH_TIME"
const val MY_LIST_REFRESH_TIME = "MY_LIST_REFRESH_TIME"

// JsonConverter
object JsonConverter {
    @OptIn(ExperimentalSerializationApi::class)
    val Json: Json = Json {
        // 忽略实体类中未出现的json键值
        ignoreUnknownKeys = true
        // 忽略实体类空值
        explicitNulls = false
        // 不编码默认值
        encodeDefaults = false
        // 忽略json空值
        coerceInputValues = true
    }

}