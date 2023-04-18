package com.lemon.smartcampus.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lemon.smartcampus.data.database.entities.CourseEntity

@Dao
interface CourseDao {
    @Query("SELECT * FROM courseEntity")
    fun getAll(): List<CourseEntity>?

    @Query("SELECT * FROM courseEntity WHERE id = :id")
    fun get(id: String): CourseEntity?

    @Query("SELECT * FROM courseEntity WHERE dayInWeek = :dInWeek AND (:week BETWEEN startWeek AND endWeek) ORDER BY id")
    fun getOneDay(dInWeek: String, week: Int): List<CourseEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(profileEntity: CourseEntity)

    @Query("DELETE FROM courseEntity WHERE id = :id")
    fun delete(id: String)

    @Query("DELETE FROM courseEntity")
    fun deleteAll()
}