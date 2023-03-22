package com.lemon.smartcampus.data.database.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.lemon.smartcampus.data.database.database.StringArrayConverter
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Entity
@Serializable
@TypeConverters(StringArrayConverter::class)
@Parcelize
data class ProfileEntity(
    @PrimaryKey val id: String,
    @ColumnInfo val phone: String,
    @ColumnInfo val token: String,
    @ColumnInfo val nickname: String,
    @ColumnInfo val background: String,
    @ColumnInfo val avatar: String,
    @ColumnInfo val tags: List<String>
) : Parcelable
