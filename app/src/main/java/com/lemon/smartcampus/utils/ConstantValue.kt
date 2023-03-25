package com.lemon.smartcampus.utils

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

// 导航
const val COVER_PAGE = "COVER_PAGE"
const val HOME_PAGE = "HOME_PAGE"
const val AUTH_PAGE = "AUTH_PAGE"
const val PUBLISH_PAGE = "PUBLISH_PAGE"
const val DETAILS_PAGE = "DETAILS_PAGE"
const val COURSE_PAGE = "COURSE_PAGE"

// 每页数据量
const val COUNT_PER_PAGE = 8

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