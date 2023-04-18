package com.lemon.smartcampus.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.lemon.smartcampus.data.database.entities.CourseGlobalSettingEntity
import com.lemon.smartcampus.data.database.entities.ProfileEntity

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profileEntity LIMIT 1")
    fun get(): ProfileEntity?

    @Insert
    fun insert(profileEntity: ProfileEntity)

    @Query("DELETE FROM profileEntity")
    fun deleteAll()
}

@Dao
interface CourseGlobalDao{
    @Query("SELECT * FROM courseGlobalSettingEntity LIMIT 1")
    fun get(): CourseGlobalSettingEntity?

    @Insert
    fun insert(courseGlobalSettingEntity: CourseGlobalSettingEntity)

    @Query("DELETE FROM courseGlobalSettingEntity")
    fun deleteAll()
}