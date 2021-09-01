package com.jinbo.newsdemo.logic.dao

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import com.jinbo.newsdemo.BaseApplication
import java.net.URL


/*******存储笑话类内容的点赞情况*******/
object IslandDao {
    private var saveTime = sharedPreferences().getInt("SavaTime", 0)
    private var islandList = ArrayList<URL>()//保存点赞了的笑话

    //执行点赞
    fun likeIslandDao(url: URL){
        islandList.add(url)
        saveTime++
        sharedPreferences().edit {
            putString("Island${saveTime}", url.toString())
            commit()
        }
        Log.e("IslandDao", "存储了点赞${url.toString()}")
    }


    //取消点赞
    fun unlikeIslandDao(url: URL): Boolean{

        Log.e("unlike", "点赞项${url.toString()}已取消")
        Log.e("unlike", "未找到点赞项")
        return false
    }

    //读取点赞数据
    fun getIslandDao(): ArrayList<URL>{
        islandList.clear()
        for (i in 1..saveTime){
            val url = sharedPreferences().getString("Island${i}", "")
            islandList.add(URL(url))
        }
        return islandList
    }


    //判断是否点赞
    fun isLike(url : URL): Boolean{
        if (islandList.size == 0) islandList = getIslandDao()
        for (i in 0 until islandList.size){
            if (islandList[i] == url){
                Log.e("isLike", "${url.toString()}点过赞")
                return true
            }
        }
        return false
    }

    fun saveIsland(){
        sharedPreferences().edit().putInt("SaveTime", saveTime).apply()
    }

    //创建SharedPreferences实例
    private fun sharedPreferences() = BaseApplication.context.getSharedPreferences("islandDao", Context.MODE_PRIVATE)
}