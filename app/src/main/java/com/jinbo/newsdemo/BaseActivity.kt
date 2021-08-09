package com.jinbo.newsdemo

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity

class BaseActivity : Application(){
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        const val APPKEY = "e02a9ae712facdd3"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

}