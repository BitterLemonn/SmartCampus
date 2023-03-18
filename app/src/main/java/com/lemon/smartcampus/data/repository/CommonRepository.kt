package com.lemon.smartcampus.data.repository

class CommonRepository() {
    companion object {
        @Volatile
        private var instance: CommonRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: CommonRepository().also { instance = it }
            }
    }
}