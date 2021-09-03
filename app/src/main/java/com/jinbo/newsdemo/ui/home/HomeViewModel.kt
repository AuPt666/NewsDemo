package com.jinbo.newsdemo.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.jinbo.newsdemo.BaseApplication
import com.jinbo.newsdemo.logic.Repository
import com.jinbo.newsdemo.logic.model.NewsResponse

/***********主页ViewModel**************/
class HomeViewModel : ViewModel() {

    private val newsResponseLiveData = MutableLiveData<String>()

    val newsList = ArrayList<NewsResponse.Detail>()

    val newsLiveData = Transformations.switchMap(newsResponseLiveData) { channel ->
        Log.e("HomeViewModel", "请求$channel")
        Repository.getNews(channel, 10)
    }

    fun getNews(channel: String){
        newsResponseLiveData.value = channel
    }

    fun saveHistory(detail: NewsResponse.Detail, context: Context) = Repository.saveHistory(detail, context)

    fun getHistory(context: Context): List<NewsResponse.Detail> = Repository.getHistory(context)

    fun isHistorySaved(): Boolean = Repository.isHistorySave()

}