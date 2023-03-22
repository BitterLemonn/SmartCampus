package com.lemon.smartcampus.data.database.networkEntities

import androidx.room.TypeConverters
import com.lemon.smartcampus.data.database.database.StringArrayConverter
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