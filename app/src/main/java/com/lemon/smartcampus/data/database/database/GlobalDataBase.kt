package com.lemon.smartcampus.data.database.database

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class GlobalDataBase : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        lateinit var database: AppDataBase
    }

    override fun onCreate() {
        super.onCreate()
        // 初始化数据库
        context = applicationContext
        database = AppDataBase.getInstance(context)
    }

}