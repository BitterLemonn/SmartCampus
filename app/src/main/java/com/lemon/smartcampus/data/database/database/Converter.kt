package com.lemon.smartcampus.data.database.database

import androidx.room.TypeConverter
import com.lemon.smartcampus.ui.widges.ResType
import com.lemon.smartcampus.utils.JsonConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.util.*

class StringArrayConverter {
    @TypeConverter
    fun decodeStringArrayData(data: String?): List<String> {
        return if (data == null || data.isBlank())
            Collections.emptyList()
        else
            JsonConverter.Json.decodeFromString(data)
    }

    @TypeConverter
    fun encodeStringArrayData(data: List<String>): String {
        return JsonConverter.Json.encodeToString(data)
    }
}

class StringLongMapConverter{
    @TypeConverter
    fun decodeMapData(data: String?): Map<String, Long> {
        return if (data == null || data.isBlank())
            mutableMapOf()
        else
            JsonConverter.Json.decodeFromString(data)
    }

    @TypeConverter
    fun encodeStringArrayData(data: Map<String, Long>): String {
        return JsonConverter.Json.encodeToString(data)
    }
}