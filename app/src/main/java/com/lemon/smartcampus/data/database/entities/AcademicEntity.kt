package com.lemon.smartcampus.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
@kotlinx.serialization.Serializable
data class AcademicEntity(
    @PrimaryKey val id: String = "",
    @ColumnInfo val informationTitle: String = "",
    @ColumnInfo val informationContent: String = "",
    @ColumnInfo val publishDate: String = "1980-01-01"
)
