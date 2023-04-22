package com.lemon.smartcampus.data.database.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.lemon.smartcampus.data.database.database.StringArrayConverter
import com.lemon.smartcampus.ui.widges.ResType
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@TypeConverters(StringArrayConverter::class)
@Parcelize
@Entity
data class TopicEntity(
    @PrimaryKey val topicId: String = "",
    @ColumnInfo val userId: String = "",
    @ColumnInfo val topicContent: String = "",
    @ColumnInfo val nickname: String = "",
    @ColumnInfo val avatar: String = "",
    @ColumnInfo val publishTime: String = "",
    @ColumnInfo val topicTag: List<String> = listOf(),
    @ColumnInfo val commentCount: Int = 0,
    @ColumnInfo val resourceName: String = "",
    @ColumnInfo val resourceType: Int = ResType.UNKNOWN,
    @ColumnInfo val resourceSize: Float = 0f,
    @ColumnInfo val resourceLink: String = "",
    @ColumnInfo val hasRes: Boolean = resourceSize > 0f,
    @ColumnInfo var isCached: Boolean = false
) : Parcelable

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