package com.jinbo.newsdemo.logic.dao
import android.content.Context
import android.util.Log
import androidx.core.content.edit
import com.google.gson.Gson
import com.jinbo.newsdemo.BaseApplication
import com.jinbo.newsdemo.logic.model.NewsResponse

/*******存储新闻类内容的点击历史*******/
object NewsDao {
    private var saveTime = 0//保存数据的次数
    private var newsList = ArrayList<NewsResponse.Detail>()
    //保存数据
    fun saveNewsDao(newsResult: NewsResponse.Detail) {
        var flag = true
        for (i in 0 until newsList.size){
            if (newsList[i].title == newsResult.title) flag = false
        }
        if (flag){
            newsList.add(newsResult)
            sharedPreferences().edit {
                putString("News${saveTime}", Gson().toJson(newsResult))//将数据转换为Json后存入SharedPreferences
            }
            saveTime++
            Log.e("NewsDao", "这是第${saveTime}条存入的数据")
        }else{
            Log.e("NewsDao", "存在相同的数据")
        }
    }


    //读取数据
    fun getNewsDao(): List<NewsResponse.Detail>{
        newsList.clear()
        for (i in 0 until saveTime){
            //取出数据并加入NewsList
            val newsJson = sharedPreferences().getString("News${i}"," ")!!
            newsList.add(Gson().fromJson(newsJson, NewsResponse.Detail::class.java))
        }
        Log.e("NewsDao", "读取到${newsList.size}条数据")
        return newsList
    }

    //判断SharedPreferences是否为空
    fun isNewsDaoSaved(): Boolean = sharedPreferences().contains("Name0")

    //创建SharedPreferences实例
    private fun sharedPreferences() = BaseApplication.context.getSharedPreferences("NewsDemo", Context.MODE_PRIVATE)

}