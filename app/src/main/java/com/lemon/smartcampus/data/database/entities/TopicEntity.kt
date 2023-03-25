package com.lemon.smartcampus.data.database.entities

import android.os.Parcelable
import androidx.room.TypeConverters
import com.lemon.smartcampus.data.database.database.StringArrayConverter
import com.lemon.smartcampus.ui.widges.ResType
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@TypeConverters(StringArrayConverter::class)
@Parcelize
data class TopicEntity(
    val topicId: String,
    val userId: String,
    val topicContent: String,
    val nickname: String,
    val avatar: String,
    val publishTime: String,
    val topicTag: List<String>,
    val commentCount: Int = 0,
    val resourceName: String = "",
    val resourceType: Int = ResType.UNKNOWN,
    val resourceSize: Float = 0f,
    val resourceLink: String = "",
    val hasRes: Boolean = resourceSize > 0f,
) : Parcelable {
    companion object {
        fun getEmpty(): TopicEntity {
            return TopicEntity(
                topicId = "",
                userId = "",
                avatar = "",
                nickname = "",
                publishTime = "",
                topicContent = "",
                topicTag = listOf()
            )
        }
    }
}

@Serializable
data class CommentEntity(
    val avatar: String,
    val nickname: String,
    val commentTime: String,
    val comment: String,
    val userId: String,
    val topicId: String,
    val commentId: String
)