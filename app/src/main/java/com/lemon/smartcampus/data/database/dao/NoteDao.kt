package com.lemon.smartcampus.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lemon.smartcampus.data.database.entities.NoteEntity

@Dao
interface NoteDao {
    @Query("SELECT * FROM noteEntity ORDER BY id")
    fun getAll(): List<NoteEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(noteEntity: NoteEntity)

    @Query("DELETE FROM noteEntity WHERE id=:id")
    fun delete(id: String)

    @Query("DELETE FROM noteEntity")
    fun deleteAll()
}