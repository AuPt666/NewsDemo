package com.jinbo.newsdemo

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity

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

    }
}