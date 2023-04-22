package com.lemon.smartcampus.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lemon.smartcampus.data.database.entities.TopicEntity

@Dao
interface UserResDao {

    @Query("SELECT * FROM topicEntity WHERE resourceSize > 0 AND isCached = false")
    fun getAll(): List<TopicEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(postList: List<TopicEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: TopicEntity)

    @Query("DELETE FROM topicEntity WHERE resourceSize > 0 AND isCached = false")
    fun deleteAll()

    @Query("DELETE FROM topicEntity WHERE topicId = :topicId")
    fun delete(topicId: String)
}

@Dao
interface UserTopicDao {

    @Query("SELECT * FROM topicEntity WHERE resourceSize <= 0 AND isCached = false")
    fun getAll(): List<TopicEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(postList: List<TopicEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: TopicEntity)

    @Query("DELETE FROM topicEntity WHERE resourceSize <= 0 AND isCached = false")
    fun deleteAll()

    @Query("DELETE FROM topicEntity WHERE topicId = :topicId")
    fun delete(topicId: String)
}

@Dao
interface CachedPostDao{
    @Query("SELECT * FROM topicEntity WHERE resourceSize <= 0 AND isCached = true")
    fun getTopicAll(): List<TopicEntity>?

    @Query("SELECT * FROM topicEntity WHERE resourceSize > 0 AND isCached = true")
    fun getResAll(): List<TopicEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list : List<TopicEntity>)

    @Query("DELETE FROM topicEntity WHERE isCached = true")
    fun deleteAll()
}