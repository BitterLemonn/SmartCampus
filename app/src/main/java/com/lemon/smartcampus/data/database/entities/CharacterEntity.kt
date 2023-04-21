package com.lemon.smartcampus.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class CharacterEntity(
    @PrimaryKey val id: String = "",
    @ColumnInfo val name: String = "",
    @ColumnInfo val imgUrl: String = "",
    @ColumnInfo val introduction: String = ""
)
