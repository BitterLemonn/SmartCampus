package com.lemon.smartcampus.data.database.networkEntities

import kotlinx.serialization.Serializable

@Serializable
data class PasswordLoginEntity(
    val phone: String,
    val password: String
)

@Serializable
data class CodeLoginEntity(
    val phone: String,
    val code: String
)

@Serializable
data class RegisterEntity(
    val phone: String,
    val password: String,
    val code: String
)

@Serializable
data class ChangeNicknameEntity(
    val userId: String,
    val nickname: String
)

@Serializable
data class ChangeTagsEntity(
    val userId: String,
    val tags: List<String>
)