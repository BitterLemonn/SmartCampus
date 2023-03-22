package com.lemon.smartcampus.data.database.entities

import com.lemon.smartcampus.ui.widges.ResType

data class TopicEntity(
    val userID: String,
    val iconUrl: String,
    val nickName: String,
    val date: String,
    val content: String,
    val tags: List<String>,
    val commentCount: Int = 0,
    val hasRes: Boolean = false,
    val resName: String = "",
    val resType: ResType = ResType.UNKNOWN,
    val resSize: Float = 0f,
    val resLink: String = "",
) {
    companion object {
        fun getEmpty(): TopicEntity {
            return TopicEntity(
                userID = "",
                iconUrl = "",
                nickName = "",
                date = "",
                content = "",
                tags = listOf()
            )
        }
    }
}

data class CommentEntity(
    val iconUrl: String,
    val nickName: String,
    val date: String,
    val content: String
)