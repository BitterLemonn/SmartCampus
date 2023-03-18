package com.lemon.smartcampus.data.entities

data class ProfileEntity(
    val id: String,
    val nickname: String,
    val bgImgUrl: String,
    val iconUrl: String,
    val tags: List<String>
)
