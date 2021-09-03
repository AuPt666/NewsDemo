package com.jinbo.newsdemo.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.jinbo.newsdemo.BaseApplication
import com.jinbo.newsdemo.logic.model.UserData

object UserDataDao {

    fun saveUserData(userData: UserData){
        sharedPreferences().edit {
            putString("userDataDaoName", userData.name)
            putString("userDataDaoIntroduction", userData.introduction)
            putString("userDataDaoGender", userData.gender)
        }
    }

    //保存用户姓名
    fun saveUserName(userName: String){
        sharedPreferences().edit {
            putString("userDataDaoName", userName)
        }
    }

    //保存用户简介
    fun saveUserIntroduction(introduction: String){
        sharedPreferences().edit {
            putString("userDataDaoIntroduction", introduction)
        }
    }

    //保存用户性别
    fun saveUserGender(userGender: String){
        sharedPreferences().edit {
            putString("userDataDaoGender", userGender)
        }
    }

    //读取用户姓名、性别、简介
    fun getUserName() = sharedPreferences().getString("userDataDaoName", "用户名")
    fun getUserIntroduction() = sharedPreferences().getString("userDataDaoIntroduction", "用户信息简述")
    fun getUserGender() = sharedPreferences().getString("userDataDaoGender","保密")

    //创建SharedPreferences实例
    private fun sharedPreferences() = BaseApplication.context.getSharedPreferences("userDataDao", Context.MODE_PRIVATE)
}