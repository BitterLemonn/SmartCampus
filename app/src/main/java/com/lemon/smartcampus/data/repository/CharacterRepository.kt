package com.lemon.smartcampus.data.repository

import com.lemon.smartcampus.data.api.CharacterApi
import com.lemon.smartcampus.data.database.entities.CharacterEntity
import com.lemon.smartcampus.utils.NetworkState
import com.lemon.smartcampus.utils.UnifiedExceptionHandler

class CharacterRepository {
    companion object {
        @Volatile
        private var instance: CharacterRepository? = null
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: CharacterRepository().also { instance = it }
        }
    }

    suspend fun getScholarList(): NetworkState<List<CharacterEntity>>{
        return UnifiedExceptionHandler.handleSuspend { CharacterApi.create().getScholarList() }
    }

    suspend fun getCharacterList(): NetworkState<List<CharacterEntity>>{
        return UnifiedExceptionHandler.handleSuspend { CharacterApi.create().getCharacterList() }
    }
}