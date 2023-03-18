package com.lemon.smartcampus.data.entities

import com.lemon.smartcampus.ui.widges.ResType

data class TopicEntity(
    val iconUrl: String,
    val nickName: String,
    val date: String,
    val content: String,
    val tag: List<String>,
    val commentCount: Int = 0,
    val hasRes: Boolean = false,
    val resName: String = "",
    val resType: ResType = ResType.UNKNOWN,
    val resSize: Float = 0f,
    val resLink: String = "",
)