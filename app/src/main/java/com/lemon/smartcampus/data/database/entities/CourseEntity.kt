package com.lemon.smartcampus.data.database.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class CourseEntity(
    @ColumnInfo val startTime: String,
    @ColumnInfo val endTime: String,
    @ColumnInfo val startWeek: Int,
    @ColumnInfo val endWeek: Int,
    @ColumnInfo val dayInWeek: String,
    @ColumnInfo val name: String,
    @ColumnInfo val location: String,
    @ColumnInfo val needAlarm: Boolean,
    @ColumnInfo val alarmTime: Int,
    @PrimaryKey val id: String
) : Parcelable
