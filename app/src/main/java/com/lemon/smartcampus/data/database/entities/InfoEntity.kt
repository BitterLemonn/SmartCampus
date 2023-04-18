package com.lemon.smartcampus.data.database.entities

@kotlinx.serialization.Serializable
data class InfoEntity<T>(
    val totalCount: Int,
    val pageSize: Int,
    val totalPage: Int,
    val currPage: Int,
    val list: List<T>
)