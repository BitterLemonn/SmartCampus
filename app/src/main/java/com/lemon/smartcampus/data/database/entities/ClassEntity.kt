package com.lemon.smartcampus.data.database.entities

data class ClassEntity(
    val startTime: String,
    val endTime: String,
    val name: String,
    val location: String,
    val needAlarm: Boolean,
    val alarmTime: Int
)
