package com.lemon.smartcampus.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lemon.smartcampus.data.database.entities.AcademicEntity
import com.lemon.smartcampus.data.database.entities.NewsEntity

@Dao
interface InfoDao {
    @Query("SELECT * FROM newsEntity LIMIT 4")
    fun getHomePageNews(): List<NewsEntity>?

    @Query("SELECT * FROM academicEntity LIMIT 4")
    fun getHomePageAcademic(): List<AcademicEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAcademicList(academicList: List<AcademicEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewsList(newsList: List<NewsEntity>)

    @Query("DELETE FROM academicEntity")
    fun deleteAcademicAll()

    @Query("DELETE FROM newsEntity")
    fun deleteNewsAll()

    @Query("SELECT * FROM newsEntity WHERE id = :id")
    fun getNewsById(id: String): NewsEntity?

    @Query("SELECT * FROM academicEntity WHERE id = :id")
    fun getAcademicById(id: String): AcademicEntity?
}