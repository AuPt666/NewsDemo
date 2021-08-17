package com.jinbo.newsdemo.logic.dao
import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.jinbo.newsdemo.BaseActivity
import com.jinbo.newsdemo.logic.model.NewsResponse

object NewsDao {

    //保存数据
    fun saveNewsDao(newsResult: NewsResponse.Detail) {
        sharedPreferences().edit {
            putString("News${NewsList.size}", Gson().toJson(newsResult))
        }
    }

    //读取数据
    fun getNewsDao(): List<NewsResponse.Detail>{
        var newsJson: String = ""
        for (i in 0 until NewsList.size)
        newsJson = sharedPreferences().getString("Name${i}"," ")!!
        NewsList.add(Gson().fromJson(newsJson, NewsResponse.Detail::class.java))
        return NewsList
    }


    fun isNewsDaoSaved(): Boolean = sharedPreferences().contains("Name0")


    private fun sharedPreferences() = BaseActivity.context.getSharedPreferences("NewsDemo", Context.MODE_PRIVATE)

    private val NewsList = ArrayList<NewsResponse.Detail>()
}