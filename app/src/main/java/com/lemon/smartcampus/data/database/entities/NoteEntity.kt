package com.lemon.smartcampus.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NoteEntity(
    @PrimaryKey val id: String = "",
    @ColumnInfo val title: String = "",
    @ColumnInfo val content: String = "",
    @ColumnInfo val createTime: String = ""
)