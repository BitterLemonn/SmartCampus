package com.lemon.smartcampus.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.lemon.smartcampus.data.database.entities.ProfileEntity

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profileEntity")
    fun getAll(): List<ProfileEntity>?

    @Insert
    fun insert(profileEntity: ProfileEntity)

    @Query("DELETE FROM profileEntity")
    fun deleteAll()
}