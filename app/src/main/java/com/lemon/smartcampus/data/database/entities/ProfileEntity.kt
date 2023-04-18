package com.lemon.smartcampus.data.database.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.lemon.smartcampus.data.database.database.StringArrayConverter
import com.lemon.smartcampus.data.database.database.StringLongMapConverter
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Entity
@Serializable
@TypeConverters(StringArrayConverter::class, StringLongMapConverter::class)
@Parcelize
data class ProfileEntity(
    @PrimaryKey val id: String,
    @ColumnInfo val phone: String,
    @ColumnInfo val token: String,
    @ColumnInfo val nickname: String,
    @ColumnInfo val background: String,
    @ColumnInfo val avatar: String,
    @ColumnInfo val tags: List<String>,
    @ColumnInfo val refreshTime: MutableMap<String, Long> = mutableMapOf()
) : Parcelable

@Entity
@Serializable
data class CourseGlobalSettingEntity(
    @PrimaryKey val id: String = "${System.currentTimeMillis()}",
    @ColumnInfo val totalWeek: Int,
    @ColumnInfo val singleTime: Int,
    @ColumnInfo val morningClass: Int,
    @ColumnInfo val noonClass: Int,
    @ColumnInfo val nightClass: Int,
    @ColumnInfo val breakTime: Int,
    @ColumnInfo val noonBreakTime: Int,
    @ColumnInfo val nightBreakTime: Int,
    @ColumnInfo val weekendClass: Boolean = false
)
