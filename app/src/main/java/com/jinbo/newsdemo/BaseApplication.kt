package com.jinbo.newsdemo

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.jinbo.newsdemo.logic.dao.IslandDao
import com.jinbo.newsdemo.logic.dao.NewsDao

/***********基础Application**************/
class BaseApplication : Application(){
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context//全局上下文

        const val APPKEY: String = "e02a9ae712facdd3"//APPKey
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    override fun onTerminate() {
        super.onTerminate()
        NewsDao.saveNews()
        IslandDao.saveIsland()
    }
}