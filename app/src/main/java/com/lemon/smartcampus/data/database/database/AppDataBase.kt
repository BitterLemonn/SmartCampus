package com.lemon.smartcampus.data.database.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lemon.smartcampus.data.database.dao.*
import com.lemon.smartcampus.data.database.entities.*

@Database(
    entities = [
        ProfileEntity::class,
        TopicEntity::class,
        CourseEntity::class,
        CourseGlobalSettingEntity::class,
        NewsEntity::class,
        AcademicEntity::class,
        NoteEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {
    companion object {
        @Volatile
        private var instance: AppDataBase? = null

        private const val DATABASE_NAME = "pokeWiki.db"

        fun getInstance(context: Context): AppDataBase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(context, AppDataBase::class.java, DATABASE_NAME)
                    .build().also { instance = it }
            }
        }
    }

    abstract fun profileDao(): ProfileDao

    abstract fun resDao(): UserResDao

    abstract fun topicDao(): UserTopicDao

    abstract fun courseDao(): CourseDao

    abstract fun courseGlobalDao(): CourseGlobalDao

    abstract fun infoDao(): InfoDao

    abstract fun noteDao(): NoteDao

}