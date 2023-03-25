package com.lemon.smartcampus.data.database.networkEntities

import androidx.room.TypeConverters
import com.lemon.smartcampus.data.database.database.StringArrayConverter
import kotlinx.serialization.Serializable

@TypeConverters(StringArrayConverter::class)
@Serializable
data class TopicPublishEntity(
    val userId: String,
    val topicContent: String,
    val topicTags: List<String>
)

@Serializable
data class TopicCommentEntity(
    val userId: String,
    val topicId: String,
    val comment: String
)
