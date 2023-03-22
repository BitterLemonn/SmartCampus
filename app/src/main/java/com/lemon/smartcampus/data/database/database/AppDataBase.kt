package com.lemon.smartcampus.data.database.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lemon.smartcampus.data.database.dao.ProfileDao
import com.lemon.smartcampus.data.database.entities.ProfileEntity

@Database(
    entities = [
        ProfileEntity::class
    ],
    version = 1,
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
}