package com.jinbo.newsdemo.logic.model

import android.graphics.Bitmap

data class UserData(var name: String = "用户名", var introduction: String = "用户信息简述", var gender: String = "保密", var headPhoto: Bitmap)