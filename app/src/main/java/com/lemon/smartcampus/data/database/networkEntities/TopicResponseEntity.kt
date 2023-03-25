package com.lemon.smartcampus.data.database.networkEntities

import com.lemon.smartcampus.data.database.entities.CommentEntity
import com.lemon.smartcampus.data.database.entities.TopicEntity

@kotlinx.serialization.Serializable

data class TopicResponseEntity(
    val totalCount:Int,
    val pageSize:Int,
    val totalPage:Int,
    val currPage:Int,
    val list: List<TopicEntity>
)

@kotlinx.serialization.Serializable
data class CommentResponseEntity(
    val totalCount:Int,
    val pageSize:Int,
    val totalPage:Int,
    val currPage:Int,
    val list: List<CommentEntity>
)
